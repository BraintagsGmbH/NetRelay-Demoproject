package de.braintags.netrelay.fairytale.model;

import java.sql.Timestamp;

import de.braintags.io.vertx.pojomapper.annotation.Entity;
import de.braintags.io.vertx.pojomapper.annotation.field.Id;
import de.braintags.io.vertx.pojomapper.annotation.lifecycle.BeforeSave;
import io.vertx.docgen.Source;

@Source(translate = false)
@Entity
public class FairyTale {
  @Id
  public String id;
  public String name;
  public String description;
  public Timestamp createdOn = new Timestamp(System.currentTimeMillis());
  public Timestamp modifiedOn;

  @BeforeSave
  public void triggerBeforeSave() {
    this.modifiedOn = new Timestamp(System.currentTimeMillis());
  }

}
