package annotation.basic;

@AnnoMeta //타입에 적용
public class MetaData {
    //@AnnoMeta //필드에 적용 불가
    private String id;

    @AnnoMeta //메서드에 적용
    public void call() {
        System.out.println("MetaData.call");
    }

    public static void main(String[] args) throws NoSuchMethodException {
        AnnoMeta typeAnno = MetaData.class.getAnnotation(AnnoMeta.class);
        System.out.println("typeAnno = " + typeAnno);

        AnnoMeta methodAnno = MetaData.class.getMethod("call").getAnnotation(AnnoMeta.class);
        System.out.println("methodAnno = " + methodAnno);
    }
}
