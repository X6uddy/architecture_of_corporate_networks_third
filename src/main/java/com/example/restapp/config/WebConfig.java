package com.example.restapp.config;

import com.example.restapp.model.Artist;
import com.example.restapp.model.Picture;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;

import java.io.IOException;
import java.util.List;

@Configuration
public class WebConfig
{
    @Bean
    public MappingJackson2XmlHttpMessageConverter xmlConverter()
    {
        XmlMapper xmlMapper = new XmlMapper()
        {
            protected void _configAndWriteValue(JsonGenerator g, Object value, String rootName) throws IOException
            {
                String xslHref = "/pictures.xsl";  // По умолчанию для списков картин

                // Динамический выбор XSL на основе типа value и rootName
                if (value instanceof List)
                {
                    // Для списков (ArrayList в XML)
                    if (rootName != null && rootName.contains("Picture"))
                    {
                        xslHref = "/pictures.xsl";
                    }
                    else if (rootName != null && rootName.contains("Artist"))
                    {
                        xslHref = "/artists.xsl";
                    }
                }
                else
                {
                    // Для одиночных объектов
                    if (value instanceof Picture)
                    {
                        xslHref = "/picture.xsl";
                    }
                    else if (value instanceof Artist)
                    {
                        xslHref = "/artist.xsl";
                    }
                }

                // Добавляем Processing Instruction
                g.writeRaw("<?xml-stylesheet type=\"text/xsl\" href=\"" + xslHref + "\" ?>\n");
                super._configAndWriteValue(g, value);
            }
        };
        return new MappingJackson2XmlHttpMessageConverter(xmlMapper);
    }
}
