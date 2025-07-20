package com.example.demo.model.output;

import com.example.demo.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOutput {
    private Long id;
    private String email;
    private String name;
    private String createdAt;
    private List<Address> addresses = new ArrayList<>();
}
