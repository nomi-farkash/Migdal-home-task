import java.util.ArrayList;
import java.util.List;

public class StrictlyIncreasing {


    public static List<List<Integer>> findStrictlyIncreasing(int[] arr) {
        List<List<Integer>> result = new ArrayList<>();
        if (arr.length == 0) return result;
        //create a list of lists that will contain all the strictly increasings
        List<Integer> current = new ArrayList<>();
        //at first add to the current list ,the first place in the original array
        current.add(arr[0]);
        // a loop that over all the array:
        for (int i = 1; i < arr.length; i++) {
            //if the current number is bigger than the last one it adding it to the current list
            if (arr[i] > arr[i - 1]) {
                current.add(arr[i]);
            } else {
                //if not, checks if it is a sequence(one can not be)
                if (current.size() > 1) {
                    result.add(new ArrayList<>(current));
                }
                //clear the current list
                current.clear();
                //move to a new sequence so add to current the next number in the original array
                current.add(arr[i]);
            }
        }
        //checks if it is a sequence(one can not be)
        if (current.size() > 1) {
            result.add(new ArrayList<>(current));
        }
        return result;
    }
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 1, 2};
        List<List<Integer>> output = findStrictlyIncreasing(arr);
        for (List<Integer> seq : output) {
            System.out.println(seq);
        }
    }
}

