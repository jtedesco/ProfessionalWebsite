"""
  A queue that syncs with a given file, so it is 'persistent' beyond the process/thread
"""

import Queue, cPickle

__author__ = 'Jon Tedesco'

class PersistentQueue(Queue.Queue):
    """
      This class encapsulates a synchronized, persistent queue that stores the queue'd data using cPickle. It uses Python's
       built-in queue structure to take care of multithreading concerns

      <p> Credit goes to http://code.activestate.com/lists/python-list/310105/ for the examples of how to use cPickle in
       this implementation

       @see Queue.Queue
    """


    def __init__(self, file_name):
        """
          Constructor for this persistent queue, which assumes there is no size limit to the queue, and uses the input
           file name as the location to store the queue

           @param  file_name    the name of the file in which to store this queue persistently
           @param  max_size    the maximum capacity of the queue at any given time
        """

        self.file_name = file_name
        Queue.Queue.__init__(self, 0)    # Call the base class constructor and create an unlimited queue


    def _init(self, capacity):
        """
          Initializes the queue, overrides the base method '_init' in the Queue.Queue class, and is called when the base
           class constructor is called.

           @param  capacity the maximum size of the queue
        """

        # The capacity of the queue
        self.capacity = capacity

        # Try to load the queue from the given file, and deal with any read errors
        try:
            read_file = file(self.file_name, 'r')
            self.queue = cPickle.load(read_file)
            read_file.close()

        except IOError, err:

            # If the file doesn't exist, continue, the file will be created, otherwise, we have a problem
            if err.errno == 2:
                self.queue = []
            else:
                raise err

        except EOFError:

            #If this was the end of the file, i.e. the file was null
            self.queue = []

        # Try to write the queue to the file
        self.file = file(self.file_name, 'w')
        cPickle.dump(self.queue, self.file, 1)


    def sync(self):
        """
          Writes the queue to disk, and flushes the output buffer to ensure that the data is written
        """

        self.file.seek(0)
        cPickle.dump(self.queue, self.file, 1)
        self.file.flush()


    def _put(self, item):
        """
          Adds an item to the queue and syncs the new contents to disk. Overrides the default '_put' function of 'Queue'

           @param  item    the item to add to the queue
        """

        self.queue.append(item)
        self.sync()


    def _get(self):
        """
          Gets an item from the queue and syncs the new contents to disk. Overrides the default '_get' function of 'Queue'

           @retval the item grabbed from the queue
        """

        # Grab the item from the queue
        item = self.queue[0]
        del self.queue[0]

        # Sync this queue with disk and return the item
        self.sync()
        return item


    def contains(self, item):
        """
          Checks to see if the queue contains a given item

           @param  item    the item to check if in the queue
        """

        return item in self.queue
