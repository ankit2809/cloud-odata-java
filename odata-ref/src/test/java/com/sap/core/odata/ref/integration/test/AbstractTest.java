package com.sap.core.odata.ref.integration.test;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.sap.core.odata.api.enums.HttpStatus;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataHttpException;
import com.sap.core.odata.api.uri.UriParserException;
import com.sap.core.odata.core.rest.ODataLocatorImpl;
import com.sap.core.odata.core.rest.ODataLocatorImpl.InitParameter;
import com.sap.core.odata.ref.processor.ScenarioServiceFactory;

/**
 * @author SAP AG
 */
public abstract class AbstractTest {

  private static final ScenarioServiceFactory SERVICE_FACTORY = new ScenarioServiceFactory();

  protected Response call(final String urlString, final Map<String, String> httpHeaders, final Request request, final HttpStatus expectedStatus) throws ODataException {
    final ODataLocatorImpl oDataLocator = new ODataLocatorImpl();
    InitParameter param = oDataLocator.new InitParameter();

    param.setHttpHeaders(getHttpHeaders(httpHeaders));
    param.setPathSegments(getPathSegments(urlString));
    param.setRequest(request);
    param.setUriInfo(getUriInfo());
    param.setServiceFactory(SERVICE_FACTORY);
    oDataLocator.initializeService(param);

    try {
      final Response response = oDataLocator.handleGet();
      assertEquals(expectedStatus.getStatusCode(), response.getStatus());
      return response;
    } catch (ODataException e) {
      if (e instanceof ODataHttpException)
        assertEquals(expectedStatus, ((ODataHttpException) e).getHttpStatus());
      else if (e instanceof ODataApplicationException)
        assertEquals(expectedStatus, ((ODataApplicationException) e).getHttpStatus());
      else
        assertEquals(expectedStatus, HttpStatus.INTERNAL_SERVER_ERROR);
      return null;
    }
  }

  protected Response ok(final String urlString) throws ODataException {
    return call(urlString, null, null, HttpStatus.OK);
  }

  protected Response badRequest(final String urlString) throws ODataException {
    return call(urlString, null, null, HttpStatus.BAD_REQUEST);
  }

  protected Response notFound(final String urlString) throws ODataException {
    return call(urlString, null, null, HttpStatus.NOT_FOUND);
  }

  private HttpHeaders getHttpHeaders(final Map<String, String> httpHeaders) {
    return new HttpHeaders() {
      
      @Override
      public MultivaluedMap<String, String> getRequestHeaders() {
        return new MultivaluedHashMap<String, String>(httpHeaders);
      }
      
      @Override
      public List<String> getRequestHeader(String name) {
        return getRequestHeaders().get(name);
      }
      
      @Override
      public MediaType getMediaType() {
        return null;
      }
      
      @Override
      public Locale getLanguage() {
        return null;
      }
      
      @Override
      public Map<String, Cookie> getCookies() {
        return null;
      }
      
      @Override
      public List<MediaType> getAcceptableMediaTypes() {
        return null;
      }
      
      @Override
      public List<Locale> getAcceptableLanguages() {
        return Collections.emptyList();
      }
    };
  }

  private UriInfo getUriInfo() {
    return new UriInfo() {

      @Override
      public UriBuilder getRequestUriBuilder() {
        return null;
      }

      @Override
      public URI getRequestUri() {
        return null;
      }

      @Override
      public MultivaluedMap<String, String> getQueryParameters(final boolean decode) {
        return null;
      }

      @Override
      public MultivaluedMap<String, String> getQueryParameters() {
        return new MultivaluedHashMap<String, String>();
      }

      @Override
      public List<PathSegment> getPathSegments(final boolean decode) {
        return null;
      }

      @Override
      public List<PathSegment> getPathSegments() {
        return null;
      }

      @Override
      public MultivaluedMap<String, String> getPathParameters(final boolean decode) {
        return null;
      }

      @Override
      public MultivaluedMap<String, String> getPathParameters() {
        return null;
      }

      @Override
      public String getPath(boolean decode) {
        return null;
      }

      @Override
      public String getPath() {
        return null;
      }

      @Override
      public List<String> getMatchedURIs(final boolean decode) {
        return null;
      }

      @Override
      public List<String> getMatchedURIs() {
        return null;
      }

      @Override
      public List<Object> getMatchedResources() {
        return null;
      }

      @Override
      public UriBuilder getBaseUriBuilder() {
        return null;
      }

      @Override
      public URI getBaseUri() {
        return null;
      }

      @Override
      public UriBuilder getAbsolutePathBuilder() {
        return null;
      }

      @Override
      public URI getAbsolutePath() {
        return null;
      }
    };
  }

  private List<PathSegment> getPathSegments(final String resourcePath) throws UriParserException {
    List<PathSegment> pathSegments = new ArrayList<PathSegment>();
    for (final String segment : resourcePath.split("/", -1))
      pathSegments.add(getPathSegment(segment));
    return pathSegments;
  }

  private PathSegment getPathSegment(final String segment) {
    return new PathSegment() {

      @Override
      public String getPath() {
        return segment;
      }

      @Override
      public MultivaluedMap<String, String> getMatrixParameters() {
        return null;
      }
    };
  }
}