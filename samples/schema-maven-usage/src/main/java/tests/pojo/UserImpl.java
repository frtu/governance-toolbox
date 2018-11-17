package tests.pojo;

public class UserImpl implements UserInterface {
    private static final long serialVersionUID = 3242700573238852907L;

    private java.lang.CharSequence name;
    private java.lang.Integer favorite_number;
    private java.lang.CharSequence favorite_color;

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
    public UserImpl(java.lang.CharSequence name, java.lang.Integer favorite_number, java.lang.CharSequence favorite_color) {
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
    public java.lang.CharSequence getName() {
        return name;
    }

    /**
     * Sets the value of the 'name' field.
     *
     * @param value the value to set.
     */
    @Override
    public void setName(java.lang.CharSequence value) {
        this.name = value;
    }

    /**
     * Gets the value of the 'favorite_number' field.
     *
     * @return The value of the 'favorite_number' field.
     */
    @Override
    public java.lang.Integer getFavoriteNumber() {
        return favorite_number;
    }

    /**
     * Sets the value of the 'favorite_number' field.
     *
     * @param value the value to set.
     */
    @Override
    public void setFavoriteNumber(java.lang.Integer value) {
        this.favorite_number = value;
    }

    /**
     * Gets the value of the 'favorite_color' field.
     *
     * @return The value of the 'favorite_color' field.
     */
    @Override
    public java.lang.CharSequence getFavoriteColor() {
        return favorite_color;
    }

    /**
     * Sets the value of the 'favorite_color' field.
     *
     * @param value the value to set.
     */
    @Override
    public void setFavoriteColor(java.lang.CharSequence value) {
        this.favorite_color = value;
    }
}
