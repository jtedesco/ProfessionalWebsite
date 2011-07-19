import unittest
import os
from Common.persistent_queue import PersistentQueue
from mockito.mocking import mock
from mockito.matchers import times
from mockito.mockito import verify

__author__ = 'Jon Tedesco'

class PersistentQueueTest(unittest.TestCase):
    """
      Tests the PersistentQueue class
    """

    def test_init(self):
        """
          Tests the constructor of the persistent queue object
        """

        # Build an object with all data supplied
        test_file_name = 'test_file_name'
        persistent_queue = PersistentQueue(test_file_name)

        # Test the contents of the context
        self.assertEquals(persistent_queue.file_name, test_file_name)
        self.assertEquals(persistent_queue.queue, [])
        self.assertEquals(persistent_queue.capacity, 0)

        # Check that the file exists now
        self.assertTrue(os.path.exists(test_file_name))
        self.assertTrue(os.path.isfile(test_file_name))

        # Delete the file
        persistent_queue.file.close()
        os.remove(test_file_name)


    def test_put_calls_sync(self):
        """
          Tests that put syncs the file to disk
        """

        # Build an object with all data supplied
        test_data = (1,2,3)
        test_file_name = 'test_file_name'
        persistent_queue = PersistentQueue(test_file_name)

        # Cleanup the file
        persistent_queue.file.close()
        os.remove(test_file_name)

        # Replace the file with a mocked file
        mocked_file = mock()
        persistent_queue.file = mocked_file

        # Try performing a 'put' to the queue
        persistent_queue.put(test_data)

        # Verify that the queue is correct
        self.assertEquals(persistent_queue.queue, [test_data])

        # Verify the file was written to
        verify(mocked_file, times(1)).seek(0)
        verify(mocked_file, times(1)).flush()


    def test_get_calls_sync(self):
        """
          Tests that get syncs the file to disk
        """

        # Build an object with all data supplied
        test_file_name = 'test_file_name'
        persistent_queue = PersistentQueue(test_file_name)

        # Cleanup the file
        persistent_queue.file.close()
        os.remove(test_file_name)

        # Replace the file with a mocked file
        mocked_file = mock()
        persistent_queue.file = mocked_file

        # Fill the queue with some fake data
        persistent_queue.queue = [1]

        # Try performing a 'get' on the queue
        result = persistent_queue.get()

        # Assert the result of calling get
        self.assertEquals(result, 1)

        # Verify the file was written to
        verify(mocked_file, times(1)).seek(0)
        verify(mocked_file, times(1)).flush()


    def test_contains(self):
        """
          Tests the contains function of the persistent queue
        """

        # Build an object with all data supplied
        test_file_name = 'test_file_name'
        persistent_queue = PersistentQueue(test_file_name)

        # Initialize the queue
        persistent_queue.queue = [1, 1, 2, 3, 5, 8, 13]

        # Check that the contains works
        self.assertTrue(persistent_queue.contains(5))
        self.assertTrue(persistent_queue.contains(13))
        self.assertFalse(persistent_queue.contains(21))

        # Delete the file
        persistent_queue.file.close()
        os.remove(test_file_name)
