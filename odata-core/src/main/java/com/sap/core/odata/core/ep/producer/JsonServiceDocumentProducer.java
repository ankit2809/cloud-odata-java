package com.sap.core.odata.core.ep.producer;

import java.io.IOException;
import java.io.Writer;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.core.edm.provider.EdmImplProv;
import com.sap.core.odata.core.ep.util.FormatJson;
import com.sap.core.odata.core.ep.util.JsonStreamWriter;

/**
 * Writes the  OData service document in JSON.
 * @author SAP AG
 */
public class JsonServiceDocumentProducer {

  public static void writeServiceDocument(Writer writer, final Edm edm) throws EntityProviderException {
    final EdmProvider edmProvider = ((EdmImplProv) edm).getEdmProvider();

    JsonStreamWriter jsonStreamWriter = new JsonStreamWriter(writer);
    try {
      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.D);
      jsonStreamWriter.beginObject();
      jsonStreamWriter.name(FormatJson.ENTITY_SETS);
      jsonStreamWriter.beginArray();

      boolean first = true;
      if (edmProvider.getSchemas() != null)
        for (final Schema schema : edmProvider.getSchemas())
          if (schema.getEntityContainers() != null)
            for (final EntityContainer entityContainer : schema.getEntityContainers())
              for (final EntitySet entitySet : entityContainer.getEntitySets()) {
                if (first)
                  first = false;
                else
                  jsonStreamWriter.separator();
                jsonStreamWriter.stringValue((entityContainer.isDefaultEntityContainer() ?
                        "" : (entityContainer.getName() + Edm.DELIMITER)) + entitySet.getName());
              }
      jsonStreamWriter.endArray();
      jsonStreamWriter.endObject();
      jsonStreamWriter.endObject();
    } catch (final IOException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    } catch (final ODataException e) {
      throw new EntityProviderException(EntityProviderException.COMMON, e);
    }

  }
}