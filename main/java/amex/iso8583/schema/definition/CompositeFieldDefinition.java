package amex.iso8583.schema.definition;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public class CompositeFieldDefinition extends FieldDefinition {

    private final List<SubfieldDefinition> subfields;

    private CompositeFieldDefinition(Builder builder) {
        super(builder);
        this.subfields = Collections.unmodifiableList(
                new ArrayList<SubfieldDefinition>(
                        Objects.requireNonNull(builder.subfields, "subfields must not be null")
                )
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends FieldDefinition.Builder<Builder> {
        private List<SubfieldDefinition> subfields = Collections.emptyList();

        @Override
        protected Builder self() {
            return this;
        }

        public Builder subfields(List<SubfieldDefinition> subfields) {
            this.subfields = subfields != null ? subfields : Collections.<SubfieldDefinition>emptyList();
            return this;
        }

        @Override
        public CompositeFieldDefinition build() {
            return new CompositeFieldDefinition(this);
        }
    }
}
