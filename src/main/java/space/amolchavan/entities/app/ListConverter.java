package space.amolchavan.entities.app;

import com.creditdatamw.zerocell.converter.Converter;

import java.util.Arrays;
import java.util.List;

public class ListConverter implements Converter<List<String>> {
    @Override
    public List<String> convert(String value, String columnName, int row) {
        return Arrays.asList(value.split(","));
    }
}