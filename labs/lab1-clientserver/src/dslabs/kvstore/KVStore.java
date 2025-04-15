package dslabs.kvstore;

import dslabs.framework.Application;
import dslabs.framework.Command;
import dslabs.framework.Result;
import java.util.HashMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class KVStore implements Application {
  private final HashMap<String, String> store = new HashMap<String, String>();
  public interface KVStoreCommand extends Command {}

  public interface SingleKeyCommand extends KVStoreCommand {
    String key();

  }

  @Data
  public static final class Get implements SingleKeyCommand {
    @NonNull private final String key;

    @Override
    public boolean readOnly() {
      return true;
    }
  }

  @Data
  public static final class Put implements SingleKeyCommand {
    @NonNull private final String key, value;
  }

  @Data
  public static final class Append implements SingleKeyCommand {
    @NonNull private final String key, value;
  }

  public interface KVStoreResult extends Result {}

  @Data
  public static final class GetResult implements KVStoreResult {
    @NonNull private final String value;
  }

  @Data
  public static final class KeyNotFound implements KVStoreResult {}

  @Data
  public static final class PutOk implements KVStoreResult {}

  @Data
  public static final class AppendResult implements KVStoreResult {
    @NonNull private final String value;
  }

  // Your code here...


  @Override
  public KVStoreResult execute(Command command) {
    if (command instanceof Get) {
      Get g = (Get) command;
      String result = store.get(g.key);
      if (result == null) {
        return new KeyNotFound();
      } else {
        return new GetResult(result);
      }
    }

    if (command instanceof Put) {
      Put p = (Put) command;
      store.put(p.key, p.value);
      return new PutOk();
    }

    if (command instanceof Append) {
      Append a = (Append) command;
      String result = store.get(a.key);
      if (result != null) {
        result = result + a.value;
      } else {
        result = a.value;
      }
      store.put(a.key, result);
      return new AppendResult(result);
    }

    throw new IllegalArgumentException();
  }
}
