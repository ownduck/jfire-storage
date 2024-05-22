package io.github.ownduck.jfire.util.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StorageResult {

    private Boolean ok;

    private Long spendTime;

    private String savedPath;

    private String message;
}
