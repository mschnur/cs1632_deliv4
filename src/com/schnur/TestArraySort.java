package com.schnur;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

public class TestArraySort
{
   private static final int NUM_ARRAYS  = 150;
   private static final int MAX_ARR_LEN = 1000;
   private static List<int[]> input;
   private static List<int[]> output;

   @BeforeClass
   public static void createArrays()
   {
      Random rand = new Random(System.currentTimeMillis());

      input = new ArrayList<int[]>(NUM_ARRAYS);
      output = new ArrayList<int[]>(NUM_ARRAYS);

      for (int i = 0; i < NUM_ARRAYS; i++)
      {
         // find a random length for this array (between 0 [inclusive] and MAX_ARR_LEN [exclusive])
         // this ensures that our input has length of 0 or more, so we don't test for that
         int len = rand.nextInt(MAX_ARR_LEN);

         // create this array
         int[] in = new int[len];
         // populate this array with random values
         for (int j = 0; j < in.length; j++)
         {
            in[j] = rand.nextInt(); // this ensures that our input is all integers, so we don't test for that
         }
         // copy the array that was just created into the array that will eventually be sorted
         int[] out = Arrays.copyOf(in, in.length);
         // sort the out array
         Arrays.sort(out);
         // store both arrays
         input.add(i, in);
         output.add(i, out);
      }
   }

   @Test
   public void test_sameNumInputAndOutputArrays()
   {
      assertEquals(input.size(), output.size());
   }


   @Test
   public void test_inAndOutSameLength()
   {
      for (int i = 0; i < input.size(); i++)
      {
         assertEquals(input.get(i).length, output.get(i).length);
      }
   }

   @Test
   public void test_outputIsProperlySorted()
   {
      for (int i = 0; i < output.size(); i++)
      {
         int[] currArr = output.get(i);
         for (int j = 0; j < currArr.length - 1; j++)
         {
            assertTrue(currArr[j] <= currArr[j + 1]);
         }
      }
   }

   @Test
   public void test_allElementsInInputAccountedFor()
   {
      for (int i = 0; i < input.size(); i++)
      {
         // first count input
         int[] currInArr = input.get(i);
         Map<Integer, Integer> inputCountMap = countArrayElements(currInArr);

         // then count output
         int[] currOutArr = output.get(i);
         Map<Integer, Integer> outputCountMap = countArrayElements(currOutArr);

         // then compare
         for (Entry<Integer, Integer> entry : inputCountMap.entrySet())
         {
            assertEquals(entry.getValue(), outputCountMap.get(entry.getKey()));
         }
      }
   }

   @Test
   public void test_noNewElementsInOutput()
   {
      for (int i = 0; i < input.size(); i++)
      {
         // first count input
         int[] currInArr = input.get(i);
         Map<Integer, Integer> inputCountMap = countArrayElements(currInArr);

         // then count output
         int[] currOutArr = output.get(i);
         Map<Integer, Integer> outputCountMap = countArrayElements(currOutArr);

         // then compare
         for (Entry<Integer, Integer> entry : outputCountMap.entrySet())
         {
            assertEquals(entry.getValue(), inputCountMap.get(entry.getKey()));
         }
      }
   }

   @Test
   public void test_idempotent()
   {
      for (int i = 0; i < output.size(); i++)
      {
         int[] sorted = output.get(i);
         // copy the current output array into the array that will be re-sorted
         int[] resorted = Arrays.copyOf(sorted, sorted.length);
         // sort the copied array
         Arrays.sort(resorted);
         // compare the two arrays
         assertArrayEquals(sorted, resorted);
      }
   }

   @Test
   public void test_pure()
   {
      for (int i = 0; i < input.size(); i++)
      {
         int[] original = input.get(i);
         // copy the current input array into the array that will be sorted again
         int[] sortAgain = Arrays.copyOf(original, original.length);
         // sort the copied array
         Arrays.sort(sortAgain);
         // compare the original sorted version of the input array (i.e. the corresponding array in output)
         // and the copied array that was just sorted
         assertArrayEquals(output.get(i), sortAgain);
      }
   }

   private Map<Integer, Integer> countArrayElements(int[] array)
   {
      Map<Integer, Integer> countMap = new HashMap<>();
      for (int j = 0; j < array.length; j++)
      {
         Integer currVal = countMap.putIfAbsent(array[j], 1);
         if (currVal != null)
         {
            countMap.put(array[j], currVal.intValue() + 1);
         }
      }
      return countMap;
   }
}
