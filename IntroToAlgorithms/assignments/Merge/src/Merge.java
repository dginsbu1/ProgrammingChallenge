
public class Merge
    {
        LNode headMerge;
        LNode nodeMerge;
        // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
        public LNode mergeLists(LNode head1, LNode head2){
            headMerge = new LNode();
            nodeMerge = headMerge;
            //if one list is finished then add the rest of the other list
            merge(head1, head2);
            return headMerge;
        }
        public void merge(LNode head1, LNode head2){
            if(head1 == null){
                addAll(head2);
                return;
            }
            if(head2 == null ){
                addAll(head1);
                return;
            }
            if(head1.data < head2.data){
                nodeMerge.data = head1.data;
                nodeMerge = nodeMerge.next;
                head1 = head1.next;
            }
            else{
                System.out.println(head2.data);
                nodeMerge.data = head2.data;
                nodeMerge = nodeMerge.next;
                head2 = head2.next;
            }
            merge(head1, head2);
        }


        void addAll(LNode head){
            while(head != null){
                nodeMerge.data = head.data;
                nodeMerge = nodeMerge.next;
                head = head.next;
            }
            return;
        }

        // METHOD SIGNATURE END
class LNode{
    int data;
    LNode next;
    LNode(){
        this.next = null;
    }
    LNode( int value){
        this.data = value;
        this.next = null;
    }
}


    public static void main(String[] args) {

    }
}
