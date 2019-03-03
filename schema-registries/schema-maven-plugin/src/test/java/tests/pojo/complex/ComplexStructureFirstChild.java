package tests.pojo.complex;

public interface ComplexStructureFirstChild {
    CharSequence getName();

    void setName(CharSequence value);

    Integer getFavoriteNumber();

    void setFavoriteNumber(Integer value);

    ComplexStructureLowerChild getLowerChildOne();

    ComplexStructureLowerChild getLowerChildTwo();
}
