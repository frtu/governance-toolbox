package tests.pojo;

public class UserImpl implements UserInterface {
    private static final long serialVersionUID = 3242700573238852907L;

    private CharSequence name;
    private Integer favorite_number;
    private CharSequence favorite_color;

    /**
     * Default constructor.  Note that this does not initialize fields
     * to their default values from the schema.  If that is desired then
     * one should use <code>newBuilder()</code>.
     */
    public UserImpl() {
    }

    /**
     * All-args constructor.
     *
     * @param name            The new value for name
     * @param favorite_number The new value for favorite_number
     * @param favorite_color  The new value for favorite_color
     */
    public UserImpl(CharSequence name, Integer favorite_number, CharSequence favorite_color) {
        this.name = name;
        this.favorite_number = favorite_number;
        this.favorite_color = favorite_color;
    }

    /**
     * Gets the value of the 'name' field.
     *
     * @return The value of the 'name' field.
     */
    @Override
    public CharSequence getName() {
        return name;
    }

    /**
     * Sets the value of the 'name' field.
     *
     * @param value the value to set.
     */
    @Override
    public void setName(CharSequence value) {
        this.name = value;
    }

    /**
     * Gets the value of the 'favorite_number' field.
     *
     * @return The value of the 'favorite_number' field.
     */
    @Override
    public Integer getFavoriteNumber() {
        return favorite_number;
    }

    /**
     * Sets the value of the 'favorite_number' field.
     *
     * @param value the value to set.
     */
    @Override
    public void setFavoriteNumber(Integer value) {
        this.favorite_number = value;
    }

    /**
     * Gets the value of the 'favorite_color' field.
     *
     * @return The value of the 'favorite_color' field.
     */
    @Override
    public CharSequence getFavoriteColor() {
        return favorite_color;
    }

    /**
     * Sets the value of the 'favorite_color' field.
     *
     * @param value the value to set.
     */
    @Override
    public void setFavoriteColor(CharSequence value) {
        this.favorite_color = value;
    }
}
