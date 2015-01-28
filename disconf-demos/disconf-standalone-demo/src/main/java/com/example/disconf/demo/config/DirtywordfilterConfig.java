/*
 * Copyright (C) 2015 KNIGHT, Inc. All Rights Reserved.
 */
package com.example.disconf.demo.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

/**
 * Created by knightliao on 15/1/26.
 */
@Service
@Scope("singleton")
@DisconfFile(filename = "dirtywordfilter.properties")
public class DirtywordfilterConfig {

    private String super_dirty_word;
    private String dirty_word;
    private String kernel_keyword_include;
    private String kernel_keyword_exclude;

    @DisconfFileItem(name = "super_dirty_word", associateField = "super_dirty_word")
    public String getSuper_dirty_word() {
        return super_dirty_word;
    }

    public void setSuper_dirty_word(String super_dirty_word) {
        this.super_dirty_word = super_dirty_word;
    }

    @DisconfFileItem(name = "dirty_word", associateField = "dirty_word")
    public String getDirty_word() {
        return dirty_word;
    }

    public void setDirty_word(String dirty_word) {
        this.dirty_word = dirty_word;
    }

    @DisconfFileItem(name = "kernel_keyword_include", associateField = "kernel_keyword_include")
    public String getKernel_keyword_include() {
        return kernel_keyword_include;
    }

    public void setKernel_keyword_include(String kernel_keyword_include) {
        this.kernel_keyword_include = kernel_keyword_include;
    }

    @DisconfFileItem(name = "kernel_keyword_exclude", associateField = "kernel_keyword_exclude")
    public String getKernel_keyword_exclude() {
        return kernel_keyword_exclude;
    }

    public void setKernel_keyword_exclude(String kernel_keyword_exclude) {
        this.kernel_keyword_exclude = kernel_keyword_exclude;
    }
}
