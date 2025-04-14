package io.mingachevir.mingachevirrealestateserver.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiskSpaceInfo {
    private long totalSpace;
    private long freeSpace;
    private long usedSpace;
}
