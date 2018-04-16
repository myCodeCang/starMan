package main.qysoft.utils;

import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by kevin on 2017/10/18.
 */
public class ErrorMessages implements Iterable<String> {
    private Collection<String> errors = null;

    private ErrorMessages() {
        errors = Sets.newHashSet();
    }

    public static ErrorMessages createErrorMessages() {
        return new ErrorMessages();
    }

    public ErrorMessages add(String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            errors.add(msg);
        }
        return this;
    }

    public ErrorMessages remove(String msg) {
        if (StringUtils.isNotEmpty(msg)) {
            errors.remove(msg);
        }
        return this;
    }

    @Override
    public Iterator<String> iterator() {
        return errors.iterator();
    }

    public boolean hasErrorMessage() {
        return CollectionUtils.isEmpty(errors);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Errors: ");
        errors.forEach( error -> {
            sb.append(" - [").append(error).append("]");
        });

        return sb.toString();
    }
}
