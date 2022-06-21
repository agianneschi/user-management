package com.example.usermanagement.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.usermanagement.dto.UserDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

public class CSVUtils {

    public static String TYPE = "text/csv";
    static String[] HEADERs = { "user_name", "user_surname", "user_email", "user_password", "user_address" };

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public static List<UserDto> csvToUsersDto(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<UserDto> usersDto = new ArrayList<UserDto>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                UserDto userDto = new UserDto();
                userDto.setName(csvRecord.get("user_name"));
                userDto.setSurname(csvRecord.get("user_surname"));
                userDto.setEmail(csvRecord.get("user_email"));
                userDto.setPassword(csvRecord.get("user_password"));
                userDto.setAddress(csvRecord.get("user_address"));
                usersDto.add(userDto);
            }
            return usersDto;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}