package com.dayanfcosta.financialcontrol.commons;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author dayanfcosta
 */
public class ResourceUtils {

  private ResourceUtils() {
    super();
  }

  public static URI uri(String base, AbstractDocument document) throws URISyntaxException {
    return new URI(String.format("/%s/%s", base, document.getId()));
  }

}
