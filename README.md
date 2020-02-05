# FastBluetooth
一个快速连接蓝牙打印机的工具。

# 使用方法

## build.gradle

### Step 1. Add the JitPack repository to your build file

#### Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

### Step 2. Add the dependency

	dependencies {
    	 implementation 'com.github.chenxkang:FastBluetooth:v1.0.3'
    }


## 示例

#### 1、检查系统是否支持蓝牙
      BTManager.getDefault().isSupport()；
      
#### 2、检查蓝牙开启/关闭
      BTManager.getDefault().isEnabled()；

#### 3、初始化，注册广播、销毁广播
      BTManager.getDefault().init();
      
      BTManager.getDefault().register(this, mReceiver,
                BluetoothAdapter.ACTION_STATE_CHANGED);
                
      BTManager.getDefault().unregister(this, mReceiver);
      
#### 4、通过蓝牙地址连接打印机
      BTManager.getDefault().connect(address, new OnConnectListener() {
                  @Override
                  public void onPreConnect() {
                      // 连接之前
                  }
      
                  @Override
                  public void onPostConnect() {
                      // 连接成功
                  }
      
                  @Override
                  public void onError(String error) {
                      // 连接失败
                  }
      });

#### 5、自定义打印机指令
     List<byte[]> commands = null;
     try {
         commands = BTCommand.getDefault()
                 .init(BTCommand.CPCL, 76, 130)
                 .printLine(10, 60, 500, 60, 1)
                 .printText(BTCommand.TEXT, 10, 70, 8, 2, 2, true, "中华人民共和国")
                 .printBox(10, 130, 500, 160, 1)
                 .printText(BTCommand.TEXT_WHITE, 10, 170, 8, "我爱你")
                 .printBarcode(BTCommand.BARCODE, BTCommand.CODE128, 10, 210, 2, 1, 80, true, "20200520")
                 .printQRCode(BTCommand.BARCODE, 300, 210, 5, "我爱中国")
                 .printImage(10, 320, BitmapFactory.decodeResource(getResources(), R.drawable.icon_fu), 100, 100)
                 .commit();
     } catch (Exception e) {
         e.printStackTrace();
     }
     
#### 6、向蓝牙打印机发送数据
     BTManager.getDefault().post(address, commands, new OnResultListener() {
                 @Override
                 public void onSuccess() {
                     // 打印成功
                 }
     
                 @Override
                 public void onError(String error) {
                     // 打印失败
                 }
     });
     
