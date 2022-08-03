package rank;

public class PopRank {

    public static void main(String[] args) {
    }

    public void bubble(){
        int[] a={1,5,3,6,9,0,199,20,5};
        System.out.println("排序前");
        for (int i : a) {
            System.out.print(i+",");
        }
        int temp=0;
        for(int i=a.length;i>0;i--){
            for (int j=1;j<i;j++){
                if(a[j-1]>a[j]){
                    temp=a[j-1];
                    a[j-1]=a[j];
                    a[j]=temp;
                }
            }
        }
        System.out.println();
        System.out.println("排序后");

        for (int i : a) {
            System.out.print(i+",");
        }
    }
}
