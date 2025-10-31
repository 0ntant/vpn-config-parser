package mapper;

import app.mapper.OutlinekeysMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OutlinekeysMapperIT
{
    @Test
    void getNameFromLink_ReturnRussian ()
    {
        //given
        String linkWithCountryName = "Russia #29161";

        //then
        String linkValue = OutlinekeysMapper.linkFromCountryWithLink(linkWithCountryName);

        //expected
        assertEquals("29161", linkValue);
    }
}
