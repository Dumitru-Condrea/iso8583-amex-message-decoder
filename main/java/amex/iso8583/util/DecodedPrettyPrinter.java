package amex.iso8583.util;

import amex.iso8583.api.DecodedAmexResult;
import amex.iso8583.engine.core.DecodedFieldMeta;
import amex.iso8583.model.common.AmexMessage;
import amex.iso8583.schema.definition.CompositeFieldDefinition;
import amex.iso8583.schema.definition.FieldDefinition;
import amex.iso8583.schema.definition.SubfieldDefinition;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class DecodedPrettyPrinter {

    public interface Palette {
        String boldOn();
        String boldOff();
        String red();
        String blue();
        String magenta();
        String softMagenta();
        String resetAll();
        String dimOn();
        String dimOff();
        String fgReset();
        String gray245();
        String softBlue110();
        String softRed210();
        String green();
        String cyan();
        String yellow();
    }

    public static final class AnsiPalette implements Palette {
        @Override public String boldOn() { return "\u001B[1m"; }
        @Override public String boldOff() { return "\u001B[22m"; }
        @Override public String red() { return "\u001B[31m"; }
        @Override public String blue() { return "\u001B[1;34m"; }
        @Override public String magenta() { return "\u001B[35m"; }
        @Override public String softMagenta() { return "\u001B[38;5;177m"; }
        @Override public String resetAll() { return "\u001B[0m"; }
        @Override public String dimOn() { return "\u001B[2m"; }
        @Override public String dimOff() { return "\u001B[22m"; }
        @Override public String fgReset() { return "\u001B[39m"; }
        @Override public String gray245() { return "\u001B[38;5;245m"; }
        @Override public String softBlue110() { return "\u001B[38;5;110m"; }
        @Override public String softRed210() { return "\u001B[38;5;210m"; }
        @Override public String green() { return "\u001B[32m"; }
        @Override public String cyan() { return "\u001B[36m"; }
        @Override public String yellow() { return "\u001B[33m"; }
    }

    public static final class PlainPalette implements Palette {
        private static final String E = "";
        @Override public String boldOn() { return E; }
        @Override public String boldOff() { return E; }
        @Override public String red() { return E; }
        @Override public String blue() { return E; }
        @Override public String magenta() { return E; }
        @Override public String softMagenta() { return E; }
        @Override public String resetAll() { return E; }
        @Override public String dimOn() { return E; }
        @Override public String dimOff() { return E; }
        @Override public String fgReset() { return E; }
        @Override public String gray245() { return E; }
        @Override public String softBlue110() { return E; }
        @Override public String softRed210() { return E; }
        @Override public String green() { return E; }
        @Override public String cyan() { return E; }
        @Override public String yellow() { return E; }
    }

    public enum AnsiMode { ON, OFF }

    private static Palette S = new AnsiPalette();

    private static final int COL_SUBFIELD = 9;
    private static final int COL_TOP_HL_PAD = 2;
    private static final String DIVIDER = "\n" + repeat('-', 95);
    private static final String IND_TOP_HL = spaces(COL_TOP_HL_PAD) + "- ";
    private static final String IND_SUB = spaces(COL_SUBFIELD);
    private static final String IND_SUB_DASH_SAME_COL = spaces(Math.max(0, COL_SUBFIELD - 2)) + "- ";

    public static void setAnsiMode(AnsiMode mode) {
        setPalette(mode == AnsiMode.ON ? new AnsiPalette() : new PlainPalette());
    }

    public static void setPalette(Palette palette) {
        S = palette == null ? new PlainPalette() : palette;
    }

    public static void print(
            DecodedAmexResult<? extends AmexMessage> result,
            byte[] rawMessage,
            PrintStream out,
            List<String> highlights
    ) {
        Objects.requireNonNull(result, "result");

        if (out == null) {
            out = System.out;
        }

        Map<String, String> decodedMap = result.getFlatValues();
        String mti = result.getMessage().getMti();

        LinkedHashMap<String, String> businessKeyToPid =
                buildBusinessKeyToPid(result.getEffectiveFieldDefinitions());

        LinkedHashMap<String, String> pidToBusiness = new LinkedHashMap<>();

        for (Map.Entry<String, String> e : businessKeyToPid.entrySet()) {
            if (!pidToBusiness.containsKey(e.getValue().toUpperCase(Locale.ROOT))) {
                pidToBusiness.put(e.getValue().toUpperCase(Locale.ROOT), e.getKey());
            }
        }

        HighlightIndex hi = buildHighlightIndex(highlights, businessKeyToPid, pidToBusiness);
        Set<String> parentsWithChildren = computeParentsWithHighlightedChildren(hi, businessKeyToPid);
        Set<Integer> highlightedPositions = computeHighlightedPositions(hi, businessKeyToPid);
        Set<Integer> unknownBits = computeUnknownBits(decodedMap, businessKeyToPid);

        printHeaderBlock(out);
        boolean printedSummary = printHighlightSummary(out, highlights, businessKeyToPid, pidToBusiness, decodedMap, unknownBits);

        if (printedSummary) {
            out.println(DIVIDER);
        }

        printMainBody(out, mti, decodedMap, hi, businessKeyToPid, parentsWithChildren, highlightedPositions, unknownBits);
        printMetaBlock(out, rawMessage, result.getFieldMeta(), businessKeyToPid);
    }

    private static void printHeaderBlock(PrintStream out) {
        out.println("\n" + S.boldOn() + S.cyan() + "DECODE CONTEXT" + S.resetAll());
        out.println(repeat('-', 95));
    }

    private static LinkedHashMap<String, String> buildBusinessKeyToPid(Map<Integer, FieldDefinition> fieldDefinitions) {
        LinkedHashMap<String, String> out = new LinkedHashMap<>();
        List<Integer> keys = new ArrayList<>(fieldDefinitions.keySet());
        Collections.sort(keys);

        for (Integer fieldNumber : keys) {
            FieldDefinition fieldDefinition = fieldDefinitions.get(fieldNumber);
            String parentPid = "P" + fieldDefinition.getNumber();
            out.put(fieldDefinition.getName(), parentPid);

            if (fieldDefinition instanceof CompositeFieldDefinition) {
                CompositeFieldDefinition composite = (CompositeFieldDefinition) fieldDefinition;
                List<SubfieldDefinition> subfields = composite.getSubfields();

                for (SubfieldDefinition subfield : subfields) {
                    String subPid = parentPid + "." + subfield.getSubfieldNumber();
                    out.put(subfield.getName(), subPid);
                }
            }
        }

        return out;
    }

    private static Set<Integer> computeUnknownBits(Map<String, String> decodedMap, Map<String, String> keyToPid) {
        Set<Integer> known = new HashSet<>();

        for (String pid : new HashSet<>(keyToPid.values())) {
            Integer n = parseBaseFieldNumber(pid);
            if (n != null) {
                known.add(n);
            }
        }

        Set<Integer> on = new HashSet<>();

        String pb = decodedMap.get("PRIMARY_BITMAP");
        if (pb != null && !trimToNull(pb).isEmpty()) {
            String formatted = safeLeft(pb, 16);
            on.addAll(parseBitmapBits(formatted, "PRIMARY_BITMAP"));
        }

        String sb = decodedMap.get("SECONDARY_BITMAP");
        if (sb != null && !trimToNull(sb).isEmpty()) {
            String formatted = safeLeft(sb, 16);
            on.addAll(parseBitmapBits(formatted, "SECONDARY_BITMAP"));
        }

        Set<Integer> unknown = new HashSet<>(on);
        unknown.removeAll(known);
        unknown.remove(1);

        return unknown;
    }

    private static boolean printHighlightSummary(
            PrintStream out,
            List<String> rawHighlights,
            Map<String, String> keyToPid,
            Map<String, String> pidToBusiness,
            Map<String, String> decodedMap,
            Set<Integer> unknownBits
    ) {
        boolean printedSomething = false;

        if (rawHighlights != null && !rawHighlights.isEmpty()) {
            Set<String> seen = new HashSet<>();
            List<SummaryItem> present = new ArrayList<>();
            List<SummaryItem> absent = new ArrayList<>();

            for (String h : rawHighlights) {
                if (trimToNull(h).isEmpty()) {
                    continue;
                }

                String tok = h.trim();
                String pid = normalizePid(tok);
                String businessKey = pid != null ? pidToBusiness.get(pid) : normalizeToBusinessKey(tok);

                if (businessKey == null) {
                    continue;
                }
                if (!keyToPid.containsKey(businessKey)) {
                    continue;
                }
                if (!seen.add(businessKey)) {
                    continue;
                }

                boolean isPresent = decodedMap.containsKey(businessKey)
                        && !trimToNull(decodedMap.get(businessKey)).isEmpty();

                SummaryItem item = new SummaryItem(keyToPid.get(businessKey), businessKey);
                if (isPresent) {
                    present.add(item);
                } else {
                    absent.add(item);
                }
            }

            present.sort(Comparator.comparingInt(a -> parseBaseFieldNumberSafe(a.pid)));
            absent.sort(Comparator.comparingInt(a -> parseBaseFieldNumberSafe(a.pid)));

            if (!present.isEmpty()) {
                out.println(S.boldOn() + "Selected fields to be highlighted:" + S.boldOff());
                for (SummaryItem item : present) {
                    out.println(formatSummaryLine(item, false));
                }
                printedSomething = true;
            }

            if (!present.isEmpty() && !absent.isEmpty()) {
                out.println();
            }

            if (!absent.isEmpty()) {
                out.println(S.boldOn() + "Selected fields to be highlighted (not present in this message):" + S.boldOff());
                for (SummaryItem item : absent) {
                    out.println(formatSummaryLine(item, true));
                }
                printedSomething = true;
            }
        }

        if (unknownBits != null && !unknownBits.isEmpty()) {
            if (printedSomething) {
                out.println();
            }
            out.println(S.boldOn() + "Detected unknown bits present in bitmap:" + S.boldOff() + " " + joinUnknownBits(unknownBits));
            printedSomething = true;
        }

        return printedSomething;
    }

    private static void printMainBody(
            PrintStream out,
            String messageTypeIndicator,
            Map<String, String> decodedMap,
            HighlightIndex hi,
            LinkedHashMap<String, String> keyToPid,
            Set<String> parentsWithChildren,
            Set<Integer> highlightPositions,
            Set<Integer> unknownBits
    ) {
        printLineWithColor(out, isHighlighted(hi, "MTI", "MTI"),
                String.format("[*] MTI = %s", messageTypeIndicator));

        String pb = decodedMap.get("PRIMARY_BITMAP");
        if (pb != null && !trimToNull(pb).isEmpty()) {
            String formattedBitmap = safeLeft(pb, 16);
            List<Integer> bits = parseBitmapBits(formattedBitmap, "PRIMARY_BITMAP");
            printBitmap(out, hi, "PRIMARY_BITMAP", formattedBitmap, bits, highlightPositions, unknownBits);
        }

        String sb = decodedMap.get("SECONDARY_BITMAP");
        if (sb != null && !trimToNull(sb).isEmpty()) {
            String formattedBitmap = safeLeft(sb, 16);
            List<Integer> bits = parseBitmapBits(formattedBitmap, "SECONDARY_BITMAP");
            printBitmap(out, hi, "SECONDARY_BITMAP", formattedBitmap, bits, highlightPositions, unknownBits);
        }

        out.println();

        for (Map.Entry<String, String> e : keyToPid.entrySet()) {
            String businessKey = e.getKey();
            if ("SECONDARY_BITMAP".equals(businessKey)) {
                continue;
            }

            String pid = e.getValue();
            String val = decodedMap.get(businessKey);

            if (trimToNull(val).isEmpty()) {
                continue;
            }

            boolean isSub = businessKey.contains(".");
            String parentBusiness = isSub ? businessKey.substring(0, businessKey.indexOf('.')) : null;

            boolean ownHL = isHighlighted(hi, businessKey, pid);
            boolean parentHL = isSub && isHighlighted(hi, parentBusiness, pid.split("\\.")[0]);
            boolean parentNeedsIndent = isSub && (parentHL || parentsWithChildren.contains(parentBusiness));

            String indent;
            boolean colorizeBlue;

            if (!isSub) {
                indent = ownHL ? IND_TOP_HL : "";
                colorizeBlue = ownHL;
            } else {
                if (parentHL && ownHL) {
                    indent = IND_SUB;
                    colorizeBlue = true;
                } else if (parentHL) {
                    indent = IND_SUB;
                    colorizeBlue = false;
                } else if (ownHL && parentNeedsIndent) {
                    indent = IND_SUB_DASH_SAME_COL;
                    colorizeBlue = true;
                } else if (parentNeedsIndent) {
                    indent = IND_SUB;
                    colorizeBlue = false;
                } else if (ownHL) {
                    indent = IND_TOP_HL;
                    colorizeBlue = true;
                } else {
                    indent = "";
                    colorizeBlue = false;
                }
            }

            String line = renderFieldLine(indent, pid, businessKey, val, colorizeBlue, ownHL);
            out.println(line);
        }
    }

    private static void printMetaBlock(
            PrintStream out,
            byte[] rawMessage,
            Map<Integer, DecodedFieldMeta> fieldMeta,
            Map<String, String> businessKeyToPid
    ) {
        out.println("\n\n" + S.boldOn() + S.cyan() + "META INFORMATION" + S.resetAll());
        out.println(S.gray245() + repeat('-', 95) + S.resetAll());

        if (rawMessage != null) {
            out.println(S.boldOn() + "[RAW]" + S.boldOff() + " " + S.softBlue110() + "MESSAGE_HEX" + S.resetAll());
            List<String> chunks = splitEvery(toHex(rawMessage), 150);
            for (String chunk : chunks) {
                out.println("      " + S.dimOn() + S.softMagenta() + chunk + S.resetAll());
            }
            out.println();
        }

        if (fieldMeta != null && !fieldMeta.isEmpty()) {
            List<Map.Entry<Integer, DecodedFieldMeta>> entries =
                    new ArrayList<>(fieldMeta.entrySet());

            entries.sort(Comparator.comparingInt(Map.Entry::getKey));

            for (Map.Entry<Integer, DecodedFieldMeta> entry : entries) {
                DecodedFieldMeta meta = entry.getValue();
                String pid = findPidForFieldMeta(meta, businessKeyToPid);

                StringBuilder sb = new StringBuilder();
                sb.append(S.boldOn()).append("[").append(pid).append("]").append(S.boldOff()).append(" ");
                sb.append(S.softBlue110()).append(meta.getFieldName()).append(S.resetAll());
                sb.append("  ");
                sb.append(S.gray245()).append("start=").append(meta.getStartOffset()).append(", ");
                sb.append("end=").append(meta.getEndOffset()).append(", ");
                sb.append("len=").append(meta.getRawLength()).append(S.resetAll());
                sb.append("\n");
                sb.append("     ");
                List<String> rawChunks = splitEvery(meta.getRawHex(), 170);

                for (int i = 0; i < rawChunks.size(); i++) {
                    if (i > 0) {
                        sb.append("\n").append("     ");
                    }
                    sb.append(S.dimOn()).append(S.softMagenta()).append(rawChunks.get(i)).append(S.resetAll());
                }

                out.println(sb);
            }
        }

        out.println(DIVIDER);
    }

    private static String findPidForFieldMeta(DecodedFieldMeta meta, Map<String, String> businessKeyToPid) {
        if (meta == null || meta.getFieldName() == null) {
            return "?";
        }

        String pid = businessKeyToPid.get(meta.getFieldName());
        if (pid != null) {
            return pid;
        }

        if (meta.getFieldNumber() >= 0) {
            return "P" + meta.getFieldNumber();
        }

        return "?";
    }

    private static void printBitmap(
            PrintStream out,
            HighlightIndex hi,
            String bitmapKey,
            String hex,
            List<Integer> onBits,
            Set<Integer> highlightPositions,
            Set<Integer> unknownBits
    ) {
        boolean lineHL = isHighlighted(hi, bitmapKey, bitmapKey);
        StringBuilder sb = new StringBuilder();

        if (lineHL) {
            sb.append(S.blue());
        }

        sb.append("[*] ").append(bitmapKey).append(" = ").append(hex).append("  - ");
        sb.append(S.boldOn()).append("[");

        for (int i = 0; i < onBits.size(); i++) {
            int bit = onBits.get(i);
            boolean isHL = highlightPositions.contains(bit);
            boolean isUnknown = unknownBits != null && unknownBits.contains(bit);

            if (isHL) {
                sb.append(S.red()).append(bit);
                if (isUnknown) {
                    sb.append('*');
                }
                if (lineHL) {
                    sb.append(S.blue()).append(S.boldOn());
                } else {
                    sb.append(S.resetAll()).append(S.boldOn());
                }
            } else if (isUnknown) {
                sb.append(S.softMagenta()).append(bit).append('*');
                if (lineHL) {
                    sb.append(S.blue()).append(S.boldOn());
                } else {
                    sb.append(S.resetAll()).append(S.boldOn());
                }
            } else {
                sb.append(bit);
            }

            if (i < onBits.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("]").append(S.boldOff());

        if (lineHL) {
            sb.append(S.resetAll());
        }

        out.println(sb);
    }

    private static String renderFieldLine(
            String indent,
            String pid,
            String businessKey,
            String val,
            boolean lineBlue,
            boolean pidRedInside
    ) {
        StringBuilder sb = new StringBuilder();

        if (lineBlue) {
            sb.append(S.blue());
        }

        sb.append(indent);
        sb.append(S.boldOn()).append("[");

        if (pidRedInside) {
            sb.append(S.red()).append(pid);
            if (lineBlue) {
                sb.append(S.blue());
            } else {
                sb.append(S.resetAll());
            }
        } else {
            sb.append(pid);
        }

        sb.append("]").append(S.boldOff());
        sb.append(" ").append(businessKey).append(" = ").append(val);

        if (lineBlue) {
            sb.append(S.resetAll());
        }

        return sb.toString();
    }

    private static void printLineWithColor(PrintStream out, boolean blue, String raw) {
        out.println(blue ? (S.blue() + raw + S.resetAll()) : raw);
    }

    private static boolean isHighlighted(HighlightIndex hi, String businessKeyOrNull, String pLabelOrNull) {
        String bk = normalizeToBusinessKey(businessKeyOrNull);
        String pid = normalizePid(pLabelOrNull);
        return (bk != null && hi.byBusiness.contains(bk)) || (pid != null && hi.byPid.contains(pid));
    }

    private static Set<String> computeParentsWithHighlightedChildren(HighlightIndex hi, Map<String, String> keyToPid) {
        Set<String> parents = new HashSet<>();

        for (String bk : keyToPid.keySet()) {
            int dot = bk.indexOf('.');
            if (dot < 0) {
                continue;
            }

            String parent = bk.substring(0, dot);
            if (hi.byBusiness.contains(bk)) {
                parents.add(parent);
            }
        }

        return parents;
    }

    private static Set<Integer> computeHighlightedPositions(HighlightIndex hi, Map<String, String> keyToPid) {
        Set<Integer> res = new HashSet<>();

        for (String bk : hi.byBusiness) {
            String pid = keyToPid.get(bk);
            if (pid == null) {
                continue;
            }

            Integer base = parseBaseFieldNumber(pid);
            if (base != null) {
                res.add(base);
            }
        }

        for (String pid : hi.byPid) {
            Integer base = parseBaseFieldNumber(pid);
            if (base != null) {
                res.add(base);
            }
        }

        return res;
    }

    private static Integer parseBaseFieldNumber(String pid) {
        if (pid == null) {
            return null;
        }

        String t = pid.toUpperCase(Locale.ROOT).trim();
        if (!t.startsWith("P")) {
            return null;
        }

        int dot = t.indexOf('.');
        String main = dot < 0 ? t.substring(1) : t.substring(1, dot);

        try {
            return Integer.parseInt(main);
        } catch (NumberFormatException ignore) {
            return null;
        }
    }

    private static HighlightIndex buildHighlightIndex(
            List<String> highlights,
            Map<String, String> keyToPid,
            Map<String, String> pidToBusiness
    ) {
        if (highlights == null || highlights.isEmpty()) {
            return HighlightIndex.empty();
        }

        Set<String> byBusiness = new HashSet<>();
        Set<String> byPid = new HashSet<>();

        for (String h : highlights) {
            if (trimToNull(h).isEmpty()) {
                continue;
            }

            String t = h.trim();
            String p = normalizePid(t);

            if (p != null) {
                byPid.add(p);
                String bk = pidToBusiness.get(p);
                if (bk != null) {
                    byBusiness.add(normalizeToBusinessKey(bk));
                }
                continue;
            }

            String bk = normalizeToBusinessKey(t);
            if (bk != null) {
                byBusiness.add(bk);
                String pid = keyToPid.get(bk);
                if (pid != null) {
                    byPid.add(normalizePid(pid));
                }
            }
        }

        return new HighlightIndex(byBusiness, byPid);
    }

    private static String normalizeToBusinessKey(String key) {
        if (key == null) {
            return null;
        }

        String s = key.trim();
        if (s.isEmpty()) {
            return null;
        }

        boolean hasLowercase = false;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isLowerCase(s.charAt(i))) {
                hasLowercase = true;
                break;
            }
        }

        if (!hasLowercase) {
            return s.toUpperCase(Locale.ROOT);
        }

        String[] parts = s.split("\\.");
        StringBuilder full = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            if (i > 0) {
                full.append('.');
            }
            full.append(camelToUpperSnake(parts[i]));
        }

        return full.toString();
    }

    private static String normalizePid(String s) {
        if (s == null) {
            return null;
        }

        String t = s.trim();
        if (t.isEmpty()) {
            return null;
        }

        t = t.toUpperCase(Locale.ROOT);

        if (!t.startsWith("P")) {
            return null;
        }

        if (!t.matches("^P\\d+(\\.-?\\d+)?$")) {
            return null;
        }

        return t;
    }

    private static String camelToUpperSnake(String camel) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < camel.length(); i++) {
            char c = camel.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append('_');
            }
            sb.append(Character.toUpperCase(c));
        }

        return sb.toString();
    }

    private static List<Integer> parseBitmapBits(String hex, String bitmapType) {
        if (hex == null) {
            return Collections.emptyList();
        }

        String s = hex.replaceAll("\\s+", "").toUpperCase(Locale.ROOT);

        List<Integer> on = new ArrayList<>(64);
        int bitNo = "SECONDARY_BITMAP".equalsIgnoreCase(bitmapType) ? 65 : 1;

        for (int i = 0; i < s.length(); i++) {
            int nibble = Character.digit(s.charAt(i), 16);
            if (nibble < 0) {
                continue;
            }

            for (int b = 3; b >= 0; b--) {
                if ((nibble & (1 << b)) != 0) {
                    on.add(bitNo);
                }
                bitNo++;
            }
        }

        return on;
    }

    private static int parseBaseFieldNumberSafe(String pid) {
        if (pid == null) {
            return Integer.MAX_VALUE;
        }

        Integer val = parseBaseFieldNumber(pid);
        return val == null ? Integer.MAX_VALUE : val;
    }

    private static String formatSummaryLine(SummaryItem it, boolean absent) {
        String pidLabel = it.pid == null ? "[]" : it.pid;
        String color = absent ? S.softRed210() : S.softBlue110();
        return S.dimOn() + S.gray245() + "[" + pidLabel + "] - " + color + it.businessKey + S.resetAll();
    }

    private static String joinUnknownBits(Set<Integer> unknownBits) {
        List<Integer> ordered = new ArrayList<>(unknownBits);
        Collections.sort(ordered);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ordered.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(S.softMagenta()).append(ordered.get(i)).append('*').append(S.resetAll());
        }
        return sb.toString();
    }

    private static String trimToNull(String value) {
        return value == null ? "" : value.trim();
    }

    private static String safeLeft(String value, int max) {
        if (value == null) {
            return null;
        }
        return value.length() <= max ? value : value.substring(0, max);
    }

    private static String spaces(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private static String repeat(char ch, int times) {
        StringBuilder sb = new StringBuilder(times);
        for (int i = 0; i < times; i++) {
            sb.append(ch);
        }
        return sb.toString();
    }

    private static String toHex(byte[] value) {
        if (value == null) {
            return null;
        }

        final char[] hex = "0123456789ABCDEF".toCharArray();
        char[] out = new char[value.length * 2];
        int pos = 0;

        for (byte b : value) {
            int v = b & 0xFF;
            out[pos++] = hex[v >>> 4];
            out[pos++] = hex[v & 0x0F];
        }

        return new String(out);
    }

    private static List<String> splitEvery(String value, int chunkSize) {
        if (value == null) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        for (int i = 0; i < value.length(); i += chunkSize) {
            result.add(value.substring(i, Math.min(value.length(), i + chunkSize)));
        }
        return result;
    }

    private static final class SummaryItem {
        private final String pid;
        private final String businessKey;

        private SummaryItem(String pid, String businessKey) {
            this.pid = pid;
            this.businessKey = businessKey;
        }
    }

    private static final class HighlightIndex {
        private final Set<String> byBusiness;
        private final Set<String> byPid;

        private HighlightIndex(Set<String> byBusiness, Set<String> byPid) {
            this.byBusiness = byBusiness;
            this.byPid = byPid;
        }

        private static HighlightIndex empty() {
            return new HighlightIndex(Collections.emptySet(), Collections.emptySet());
        }
    }
}