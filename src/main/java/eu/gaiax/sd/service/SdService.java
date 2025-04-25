package eu.gaiax.sd.service;

import eu.gaiax.sd.jpa.*;
import eu.gaiax.sd.jpa.entities.FrRequest;
import eu.gaiax.sd.jpa.entities.FrRequestAttachment;
import eu.gaiax.sd.jpa.entities.FrRequestTypeEntity;
import eu.gaiax.sd.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class SdService {

  @Value("${check-keycloak-token:true}")
  private boolean checkToken;

  @Autowired
  private FrRequestDao frRequestDao;
  @Autowired
  private
  FrRequestAttachmentDao frRequestAttachmentDao;

  @Autowired
  private
  FrRequestTypeDao frRequestTypeDao;

  @Autowired
  private
  FrRequestStatusDao frRequestStatusDao;

  FrRequestTypeEntity VC_NP_NOT_CONFIRMED, VC_NP, VC_PPR_NOT_CONFIRMED,
          VC_PPR, PR_PPR, SD_SERVICE, SD_DATA, SD_NODE, SD_PPR;

  @PostConstruct
  public void initConstants() {
    VC_NP_NOT_CONFIRMED = frRequestTypeDao.findByName(FrRequestType.VC_NP_NOT_CONFIRMED.name()).orElseThrow();
    VC_NP = frRequestTypeDao.findByName(FrRequestType.VC_NP.name()).orElseThrow();
    VC_PPR_NOT_CONFIRMED = frRequestTypeDao.findByName(FrRequestType.VC_PPR_NOT_CONFIRMED.name()).orElseThrow();
    VC_PPR = frRequestTypeDao.findByName(FrRequestType.VC_PPR.name()).orElseThrow();
    PR_PPR = frRequestTypeDao.findByName(FrRequestType.PR_PPR.name()).orElseThrow();
    SD_SERVICE = frRequestTypeDao.findByName(FrRequestType.SD_SERVICE.name()).orElseThrow();
    SD_DATA = frRequestTypeDao.findByName(FrRequestType.SD_DATA.name()).orElseThrow();
    SD_NODE = frRequestTypeDao.findByName(FrRequestType.SD_NODE.name()).orElseThrow();
    SD_PPR = frRequestTypeDao.findByName(FrRequestType.SD_PPR.name()).orElseThrow();
  }

  private JSONDetails prepareJSONDetailsFromJWT(final String jwt) {
    final JSONObject json = JwtUtil.readTokenIntoJSONObject(jwt);
    log.info("json: {}", json);
    final Map<String, String> m = new HashMap<>();
    m.put("prName", json.getString("prName"));
    m.put("prEmail", json.getString("prEmail"));
    m.put("prPhone", json.getString("prPhone"));
    m.put("street_address", json.getJSONObject("address").getString("street_address"));
    m.put("postal_code", json.getJSONObject("address").getString("postal_code"));
    m.put("locality", json.getJSONObject("address").getString("locality"));
    m.put("country", json.getJSONObject("address").getString("country"));

    return new JSONDetails(m);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void processSDPostPutRequest(
          final String jwt,
          final MultipartFile file,
          final FrRequestType sdRequestType,
          final String resourceId
  ) {
    // We're storing some JWT information in details
    // Maybe this is a good place to parse SD file with external service
    // and store parsed content here additionally (or instead).
    final JSONDetails jsonDetails = prepareJSONDetailsFromJWT(jwt);
    if(resourceId != null) {
       jsonDetails.getProperties().put("resourceId", resourceId);
    }
    final FileAttachment fileAttach;
    try {
      fileAttach = new FileAttachment(file.getOriginalFilename(), file.getBytes());
    } catch (IOException e) {
      throw new RuntimeException("Can't process attachment, error: " + e);
    }

    saveSDRequest(
            sdRequestType,
            jsonDetails.getProperties().get("prEmail"),
            jsonDetails.getProperties().get("country"),
            jsonDetails,
            fileAttach,
            jsonDetails.getProperties().get("prName")
    );
  }

  private void saveSDRequest(
          final FrRequestType type,
          final String email,
          final String location,
          final JSONDetails details,
          final FileAttachment attachment,
          final String prName
  ) {
    final FrRequestTypeEntity rType;
    switch (type) {
      case SD_SERVICE:
        rType = SD_SERVICE;
        break;
      case SD_DATA:
        rType = SD_DATA;
        break;
      case SD_NODE:
        rType = SD_NODE;
        break;
      case SD_PPR:
        rType = SD_PPR;
        break;
      default:
        throw new IllegalArgumentException("Incorrect SD type provided");
    }

    final FrRequest frRequest = new FrRequest();
    frRequest.setRequestDate(Date.valueOf(LocalDate.now()));
    frRequest.setRequestType(rType);
    frRequest.setEmail(email);
    frRequest.setLocation(location);
    frRequest.setDetails(details);
    frRequest.setParticipantName(prName);


    final FrRequestAttachment frA = new FrRequestAttachment();
    frA.setFileName(attachment.getName());
    frA.setFileData(attachment.getData());
    frA.setFrRequest(frRequest);
    frRequest.setFrRequestAttachments(List.of(frA));

    frRequestDao.save(frRequest);
  }
}
