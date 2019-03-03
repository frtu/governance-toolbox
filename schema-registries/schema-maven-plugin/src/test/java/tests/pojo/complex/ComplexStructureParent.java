package tests.pojo.complex;

public interface ComplexStructureParent {
    CharSequence getName();

    void setName(CharSequence value);

    Integer getFavoriteNumber();

    void setFavoriteNumber(Integer value);

    ComplexStructureFirstChild getChildOne();
}
