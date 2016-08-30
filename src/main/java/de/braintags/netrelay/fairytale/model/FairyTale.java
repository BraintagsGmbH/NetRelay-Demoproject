package de.braintags.netrelay.fairytale.model;

import de.braintags.io.vertx.pojomapper.annotation.Entity;
import de.braintags.netrelay.model.AbstractRecord;
import io.vertx.docgen.Source;

@Source(translate = false)
@Entity
public class FairyTale extends AbstractRecord {
  public String name;
  public String description;

}
