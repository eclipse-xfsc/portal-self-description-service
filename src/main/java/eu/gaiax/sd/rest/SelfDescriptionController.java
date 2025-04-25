package eu.gaiax.sd.rest;

import eu.gaiax.sd.jpa.FrRequestType;
import eu.gaiax.sd.jpa.dto.JsonbSdData;
import eu.gaiax.sd.model.ErrorDto;
import eu.gaiax.sd.model.ResultsResponse;
import eu.gaiax.sd.model.SdDetailsDto;
import eu.gaiax.sd.service.SdService;
import eu.gaiax.sd.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/sd-service")
@Api(tags = "Self Description Service")
public class SelfDescriptionController {
  private final SdService sdService;

  @Autowired
  @Qualifier("sdSrv")
  private WebClient sdSrv;



  @Autowired
  public SelfDescriptionController(SdService sdService) {
    this.sdService = sdService;
  }

  @ApiOperation("Validation of SD file")
  @PostMapping(value = "/convert", consumes = "multipart/form-data")
  public ResponseEntity<?> convert(
          HttpServletRequest request,
          @RequestParam("descriptor_type") String param,
          @Parameter(
                  description = "File to be uploaded",
                  content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile file
  ) {
    log.info("in convert");
    final byte[] fileData;
    try {
      fileData = file.getBytes();
    } catch (IOException e) {
      return ResponseEntity
              .internalServerError()
              .body(
                      new ErrorDto(
                              "/api/sd-service/convert",
                              e.getMessage()
                      )
              );
    }

    final ResultsResponse<SdDetailsDto[]> res = ProxyCall.validateSdData(sdSrv, request, fileData, "/api/sd-service/convert");

    return ResponseEntity.ok(res);
  }

  @ApiOperation("Creation of Services")
  @PostMapping(value = "/services", consumes = "multipart/form-data")
  public ResponseEntity<?> createServices(
          HttpServletRequest request,
          @RequestHeader HttpHeaders headers,
          @Parameter(
                  description = "File to be uploaded",
                  content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile file
  ) {
    return processSDRequest("/api/sd-service/services", request, file, headers, FrRequestType.SD_SERVICE, null);
  }


  @ApiOperation("Creation of Data")
  @PostMapping(value = "/data", consumes = "multipart/form-data")
  public ResponseEntity<?> createData(
          HttpServletRequest request,
          @RequestHeader HttpHeaders headers,
          @Parameter(
                  description = "File to be uploaded",
                  content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile file
  ) {
    return processSDRequest("/api/sd-service/data", request, file, headers, FrRequestType.SD_DATA, null);
  }

  private ResponseEntity<?> processSDRequest(
          final String errorPath, final HttpServletRequest request,
          final MultipartFile file, final HttpHeaders headers,
          final FrRequestType frRequestType,
          final String resourceId
  ) {
    log.info("Request: {}; Filename: {}", request.getRequestURI(), file.getOriginalFilename());

    final String jwt = headers.getFirst(HttpHeaders.AUTHORIZATION);
    if (jwt == null || jwt.isBlank()) {
      return ResponseEntity
              .status(HttpStatus.UNAUTHORIZED)
              .body(new ErrorDto(errorPath, "Not authorized"));
    }

    try {
      sdService.processSDPostPutRequest(jwt, file, frRequestType, resourceId);
    } catch (Exception e) {
      return ResponseEntity
              .status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new ErrorDto(errorPath, e.getMessage()));
    }

    return ResponseEntity.ok().build();
  }

  @ApiOperation("Creation of Nodes")
  @PostMapping(value = "/nodes", consumes = "multipart/form-data")
  public ResponseEntity<?> createNodes(
          HttpServletRequest request,
          @RequestHeader HttpHeaders headers,
          @Parameter(
                  description = "File to be uploaded",
                  content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile file
  ) {
    return processSDRequest("/api/sd-service/nodes", request, file, headers, FrRequestType.SD_NODE, null);
  }

  @ApiOperation("Updating Services by ID")
  @PutMapping(value = "/services/{id}", consumes = "multipart/form-data")
  public ResponseEntity<?> updateServices(
          HttpServletRequest request,
          @RequestHeader HttpHeaders headers,
          @Parameter(
                  description = "File to be uploaded",
                  content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile file,
          @PathVariable String id
  ) {
    return processSDRequest("/api/sd-service/services/" + id, request, file, headers, FrRequestType.SD_SERVICE, id);
  }

  @ApiOperation("Updating Data by ID")
  @PutMapping(value = "/data/{id}", consumes = "multipart/form-data")
  public ResponseEntity<?> updateData(
          HttpServletRequest request,
          @RequestHeader HttpHeaders headers,
          @Parameter(
                  description = "File to be uploaded",
                  content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile file,
          @PathVariable String id
  ) {
    return processSDRequest("/api/sd-service/data/" + id, request, file, headers, FrRequestType.SD_DATA, id);
  }

  @ApiOperation("Updating Node by ID")
  @PutMapping(value = "/nodes/{id}", consumes = "multipart/form-data")
  public ResponseEntity<?> updateNodes(
          HttpServletRequest request,
          @RequestHeader HttpHeaders headers,
          @Parameter(
                  description = "File to be uploaded",
                  content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart(value = "file") MultipartFile file,
          @PathVariable String id
  ) {
    return processSDRequest("/api/sd-service/nodes/" + id, request, file, headers, FrRequestType.SD_NODE, id);

  }
}
