package rank;

/**
 * 快排
 * @author Van.Planifolia
 */
public class QuickShort {
    public static void main(String[] args) {
        int[] arr={7,0,41,9,6,3,1,4,5};//直接复制数组
        quickShort(0,arr.length-1,arr);
        for (int i : arr) {
            System.out.println(i);
        }
    }
    /*
    获取中间值
     */
    public static int getMid(int left,int right,int[] array){
        //设定一个标杆值可以是最左边的值也可是最右边的值比如说我们在者用最左边的值
        int temp=array[left];
        while(left<right){


            //使用while循环让所有小于temp的值都挪到他左边,否则就移动右指针
            while(array[right]>temp&&left<right){
                right--;
            }
            //将右边的值移动到左边
            array[left]=array[right];
            //交换之后开始移动左指针，让左边所有大于temp值的数移动到temp的右边，否则就持续移动左指针
            while(array[left]<temp&&left<right){
                left++;
            }
            //将左边的值移动到右边
            array[right]=array[left];
        }
        //当两个指针相遇的时候就说明遍历完毕了，然后开始返回中值。因为此时right与left相等我们直接返回任何一个
        array[right]=temp;
        return right;
    }
    /*
    快速排序的三部
    1.先根据数组里面的一个标准值可以是最左边也可以是最右边，然后进行简单的划分小于这个数的都放在它左边，大于这个数的都放在他的右边
    2.当分好左右之后就然后就通过递归来对左右两边进行进一步的快速排序
    3.然后其中一边必然会又分成两部分，然后再次通过递归来进行快排直到就剩一个单位无有法进一步排序为止
     */
    public static void quickShort(int left,int right,int[] array){
        if(left<right){
            int mid=getMid(left, right, array);
            quickShort(left,mid-1,array);
            quickShort(mid+1,right,array);
        }

    }

}