import org.csource.fastdfs.*;

public class FastExample {
    public static void main(String []agrs) throws Exception {
        ClientGlobal.init("C:\\Users\\bob\\IdeaProjects\\pingyougou\\springserivctry\\src\\main\\resources\\fdfs_client.conf");
        // 2、创建一个 TrackerClient 对象。直接 new 一个。
        TrackerClient client=new TrackerClient();
        // 3、使用 TrackerClient 对象创建连接，获得一个 TrackerServer 对象。
        TrackerServer connection = client.getConnection();
        // 4、创建一个 StorageServer 的引用，值为 null
        StorageServer storageServer=null;
      // 5、创建一个 StorageClient 对象，需要两个参数 TrackerServer 对象、StorageServer 的引用
        StorageClient storageClient=new StorageClient(connection,storageServer);
        // 6、使用 StorageClient 对象上传图片。
        String[] jpgs = storageClient.upload_file("C:\\Users\\bob\\Desktop\\7.jpg", "jpg", null);
        for (String jpg : jpgs) {
            System.out.println(jpg);
        }

    }
}
