package app.mapper;

public class OutlinekeysMapper
{
    public static String linkFromCountryWithLink(String lingValue)
    {
        return lingValue.split("#")[1];
    }
}
