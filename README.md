# SimpleImageEditor

高仿微信的图片编辑器

### 基本功能

* 涂鸦
* 贴图
* 添加文字
* 马赛克
* 截图功能
* 对操作进行撤销


![主界面](https://github.com/YanJingW/SimpleImageEditor/blob/master/assets/edit_image.png)

![涂鸦界面](https://github.com/YanJingW/SimpleImageEditor/blob/master/assets/paint.png)

![马赛克界面](https://github.com/YanJingW/SimpleImageEditor/blob/master/assets/mosaic.png)

![剪切界面](https://github.com/YanJingW/SimpleImageEditor/blob/master/assets/crop.png)

<img src="https://github.com/fazhongxu/SimpleImageEditor/blob/master/assets/shape.jpeg" width="50%" height="20%">

圆形，矩形，箭头，界面


#### 补充功能 实现画 圆形 矩形 箭头  Done

### 项目现有缺陷

* 马赛克操作有些卡顿，后续需要优化  (优化方法很简单 马赛克卡顿 只需要 把 MosaicUtil 获取 高斯模糊的方法换成 https://github.com/wasabeef/glide-transformations 里面的 高斯模糊算法 即可优化，实际项目 我已经优化，这个库没来得及修改，使用这自行优化就好)

* 裁剪的实现，没有实现在保存之前的状态的基础上进行操作。简单粗暴直接将之前所有操作生成一张图片，替换为操作图片




### 版本更新

* 1.0.0  涂鸦，贴图，添加文字，马赛克，截图功能，对操作进行撤销
* 1.0.1	 马赛克功能可以自由在不同的样式之间切换

### APK Demo

扫码下载

![image](https://github.com/YanJingW/SimpleImageEditor/blob/master/assets/edit_erweima.png )

二维码显示不出请点击 [下载Demo](https://fir.im/imageEditAndroid )
