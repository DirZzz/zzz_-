package com.sandu.api.statistics.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: chenqiang
 * @create: 2019-06-03 15:13
 */
@Data
public class TextureDto implements Serializable {
    private static final long serialVersionUID = 8338128688353453083L;

    private Integer id;

    private Integer fileSize;

    private Integer picId;

    private Integer thumbnailId;

    private Integer normalPicId;

    private Integer textureBallFileId;

    private Integer androidTextureBallFileId;

    private Integer iosTextureBallFileId;
}
