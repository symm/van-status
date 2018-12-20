package com.example.ciconnector;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BuildInfo {
  public final BuildStatus status;
}
