package nta.storage;

import java.io.IOException;

import nta.catalog.Schema;
import nta.catalog.TableMeta;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

public abstract class SingleFileAppender implements Appender{
  protected final Configuration conf;
  protected final TableMeta meta;
  protected final Schema schema;
  
  public SingleFileAppender(Configuration conf, TableMeta meta, Path path) {
    this.conf = conf;
    this.meta = meta;
    this.schema = meta.getSchema();
  }

  public abstract long getOffset() throws IOException;
}
