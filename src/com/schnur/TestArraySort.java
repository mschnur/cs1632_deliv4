package com.schnur;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

public class TestArraySort
{
   private static final int NUM_ARRAYS  = 150;
   private static final int MAX_ARR_LEN = 50000;
   private static final Random RAND = new Random(System.currentTimeMillis());

   @Test
   public void test_inAndOutSameLength()
   {
      for (int i = 0; i < NUM_ARRAYS; i++)
      {
         int[] in = createArray();
         int[] out = copy(in);
         Arrays.sort(out);
         assertEquals(in.length, out.length);
      }
   }

   @Test
   public void test_outputIsProperlySorted()
   {
      for (int i = 0; i < NUM_ARRAYS; i++)
      {
         int[] in = createArray();
         int[] out = copy(in);
         Arrays.sort(out);
         for (int j = 0; j < out.length - 1; j++)
         {
            assertTrue(out[j] <= out[j + 1]);
         }
      }
   }

   @Test
   public void test_allElementsInInputAccountedFor()
   {
      for (int i = 0; i < NUM_ARRAYS; i++)
      {
         int[] in = createArray();
         int[] out = copy(in);
         Arrays.sort(out);
         // first count input
         Map<Integer, Integer> inputCountMap = countArrayElements(in);

         // then count output
         Map<Integer, Integer> outputCountMap = countArrayElements(out);

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
      for (int i = 0; i < NUM_ARRAYS; i++)
      {
         int[] in = createArray();
         int[] out = copy(in);
         Arrays.sort(out);
         // first count input
         Map<Integer, Integer> inputCountMap = countArrayElements(in);

         // then count output
         Map<Integer, Integer> outputCountMap = countArrayElements(out);

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
      for (int i = 0; i < NUM_ARRAYS; i++)
      {
         int[] in = createArray();
         int[] out = copy(in);
         Arrays.sort(out);
         // copy the current output array into the array that will be re-sorted
         int[] resorted = copy(out);
         // sort the copied array
         Arrays.sort(resorted);
         // compare the two arrays
         assertArrayEquals(out, resorted);
      }
   }

   @Test
   public void test_pure()
   {
      for (int i = 0; i < NUM_ARRAYS; i++)
      {
         int[] in = createArray();
         int[] out = copy(in);
         Arrays.sort(out);
         // copy the current input array into the array that will be sorted again
         int[] sortAgain = copy(in);
         // sort the copied array
         Arrays.sort(sortAgain);
         // compare the original sorted version of the input array (i.e. the corresponding array in output)
         // and the copied array that was just sorted
         assertArrayEquals(out, sortAgain);
      }
   }

   private int[] createArray()
   {
      // find a random length for this array (between 0 and MAX_ARR_LEN, inclusive)
      // this ensures that our input has length of 0 or more, so we don't test for that
      int len = RAND.nextInt(MAX_ARR_LEN + 1);

      // create this array
      int[] arr = new int[len];
      // populate this array with random values
      for (int j = 0; j < arr.length; j++)
      {
         arr[j] = RAND.nextInt(); // this ensures that our input is all integers, so we don't test for that
      }

      return arr;
   }

   private int[] copy(int[] arr)
   {
      return Arrays.copyOf(arr, arr.length);
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
