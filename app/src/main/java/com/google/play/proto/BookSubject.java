// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Book.proto
package com.google.play.proto;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;

public final class BookSubject extends Message {

  public static final String DEFAULT_NAME = "";
  public static final String DEFAULT_QUERY = "";
  public static final String DEFAULT_SUBJECTID = "";

  @ProtoField(tag = 1, type = STRING)
  public final String name;

  @ProtoField(tag = 2, type = STRING)
  public final String query;

  @ProtoField(tag = 3, type = STRING)
  public final String subjectId;

  public BookSubject(String name, String query, String subjectId) {
    this.name = name;
    this.query = query;
    this.subjectId = subjectId;
  }

  private BookSubject(Builder builder) {
    this(builder.name, builder.query, builder.subjectId);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof BookSubject)) return false;
    BookSubject o = (BookSubject) other;
    return equals(name, o.name)
        && equals(query, o.query)
        && equals(subjectId, o.subjectId);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = name != null ? name.hashCode() : 0;
      result = result * 37 + (query != null ? query.hashCode() : 0);
      result = result * 37 + (subjectId != null ? subjectId.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<BookSubject> {

    public String name;
    public String query;
    public String subjectId;

    public Builder() {
    }

    public Builder(BookSubject message) {
      super(message);
      if (message == null) return;
      this.name = message.name;
      this.query = message.query;
      this.subjectId = message.subjectId;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder query(String query) {
      this.query = query;
      return this;
    }

    public Builder subjectId(String subjectId) {
      this.subjectId = subjectId;
      return this;
    }

    @Override
    public BookSubject build() {
      return new BookSubject(this);
    }
  }
}
