package mdm.out;

import java.util.Objects;

public class MorphFSAttr {
    private String name;
    private String value;

    public MorphFSAttr(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final MorphFSAttr other = (MorphFSAttr) obj;
        return Objects.equals(this.name, other.name) &&
            Objects.equals(this.value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        return Objects.toString(name) + ": " + Objects.toString(value);
    }
};
