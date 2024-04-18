package uk.co.mulecode.fileservice.controller.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class ErrorResponse {

  String message;
  String error;
  Map<String, String> errors;
  int status;
  String exception;
  long timestamp;
  String path;

}
