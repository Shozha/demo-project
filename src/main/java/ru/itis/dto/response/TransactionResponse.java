package ru.itis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private String contractName;
    //date-time
    private String from;
    //date-time
    private String to;

    private List<TransactionElementResponse> transactions;

}
