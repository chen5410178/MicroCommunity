# UploadImageUrl 组件文档

## 组件概述

`UploadImageUrl` 是一个图片上传组件，支持上传本地图片并显示预览，同时可以删除已上传的图片。

## 基本用法

```vue
<template>
  <upload-image-url 
    :image-count="3" 
    @notifyUploadCoverImage="handleImageUpload"
  />
</template>

<script>
import UploadImageUrl from '@/components/staff/UploadImageUrl.vue'

export default {
  components: {
    UploadImageUrl
  },
  methods: {
    handleImageUpload(urls) {
      // 处理上传后的图片URL数组
      console.log(urls)
    }
  }
}
</script>
```

## Props

| 参数名      | 说明               | 类型     | 默认值 |
|------------|--------------------|----------|--------|
| imageCount | 允许上传的最大图片数量 | Number   | 1      |

## Events

| 事件名                | 说明                     | 回调参数               |
|----------------------|--------------------------|-----------------------|
| notifyUploadCoverImage | 图片上传或删除时触发      | 当前所有图片的URL数组 |

## 方法

通过 ref 可以调用组件的方法：

```vue
<template>
  <upload-image-url ref="uploader" />
</template>

<script>
export default {
  methods: {
    clearImages() {
      this.$refs.uploader.clearImages()
    },
    setImages(images) {
      this.$refs.uploader.setImages(images)
    }
  }
}
</script>
```

| 方法名       | 说明                     | 参数                     |
|-------------|--------------------------|--------------------------|
| clearImages | 清空所有已上传的图片      | 无                       |
| setImages   | 设置要显示的图片          | images: Array<string> (图片URL数组) |

## 样式定制

组件使用 SCSS 编写样式，可以通过以下类名进行样式覆盖：

- `.upload-image-container` - 整个上传容器
- `.image-item` - 单个图片项容器
- `.delete-icon` - 删除按钮
- `.upload-button` - 上传按钮

## 注意事项

1. 组件限制上传图片大小不超过 2MB
2. 上传的图片会自动转换为 base64 格式进行预览
3. 组件内部会维护两个数组：
   - `photos`: 用于预览的图片数组（包含 base64 数据）
   - `photosUrl`: 上传后的图片 URL 数组
4. 当图片 URL 以 `http` 开头时，组件会直接使用该 URL 进行显示
5. 当图片数据包含 `base64,` 时，组件会将其视为 base64 数据进行显示

## 示例

### 基本使用

```vue
<upload-image-url />
```

### 限制上传数量

```vue
<upload-image-url :image-count="5" />
```

### 获取上传结果

```vue
<template>
  <upload-image-url @notifyUploadCoverImage="handleUpload" />
</template>

<script>
export default {
  methods: {
    handleUpload(urls) {
      console.log('上传的图片URL:', urls)
      // 可以将这些URL保存到表单数据中
    }
  }
}
</script>
```

### 设置初始图片

```vue
<template>
  <upload-image-url ref="uploader" />
</template>

<script>
export default {
  mounted() {
    // 设置初始图片
    this.$refs.uploader.setImages([
      'https://example.com/image1.jpg',
      'https://example.com/image2.jpg'
    ])
  }
}
</script>
```

### 清空图片

```vue
<template>
  <upload-image-url ref="uploader" />
  <button @click="clear">清空图片</button>
</template>

<script>
export default {
  methods: {
    clear() {
      this.$refs.uploader.clearImages()
    }
  }
}
</script>
```