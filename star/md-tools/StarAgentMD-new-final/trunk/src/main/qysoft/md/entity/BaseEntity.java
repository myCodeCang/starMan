package main.qysoft.md.entity;

import main.qysoft.utils.ErrorMessages;

import java.io.Serializable;

/**
 * Created by kevin on 2017/10/17.
 */
public abstract class BaseEntity implements Serializable {
    abstract protected ErrorMessages validate();
}
