"""
  This file will implement the menu for users to choose which functionality they'd like to use
"""
from string import split, replace
from copy import deepcopy

__author__ = 'Jon Tedesco'


def build_fds():
    """
      Gets and parses the list of FDs from user input
    """

    # Show the prompt and instructions
    input = str(raw_input("Please enter each FD as \"#,#,... -> #,#,#,...\", and type in \"done\" when done:\n"))

    # Get the list of fds
    replace(input, " ", "")
    fds = {}

    while input != "done":

        if "->" in input:
            # Split the input into the right and left hand sides
            sides = split(input, "->", 1)

            # Put the fds into a dictionary
            left_side = replace(sides[0], " ", "")
            right_side = replace(sides[1], " ", "")
            fds[left_side] = right_side
        else:
            input = replace(input, " ", "")
            fds[input] = input


        # Show the prompt and instructions
        input = str(raw_input("Please enter an FD as \"X,X,... -> X,X,X,...\", and type in \"done\" when done:\n"))

    return fds


def choose_elements(elements):
    """
      Lets the user choose a powerset of the previously input elements
    """

    valid = False
    while not valid:

        # Prompt for user input
        input = str(raw_input("Enter the set of elements that you would like to compute the closure of as X,X,X..., or type 'done' to return to the previous menu\n"))

        # Parse the input into a set
        element_strings = split(input, ",")
        search_elements = set()
        if type(element_strings) == type(list()):
            for element_string in element_strings:
                element_string = replace(element_string, " ", "")
                search_elements.add(element_string)
        else:
            element_strings = replace(element_strings, " ", "")
            search_elements.add(element_strings)

        # Check that the input set is a subset of the elements we have
        if not search_elements.issubset(elements):
            print "Sorry, it looks like not all those elements were input..."
            valid = False
        else:
            valid = True

    # Return the set representing the user input
    return search_elements


def convert_fds(fds):
    """
     Convert the old form of fds into a list of pairs of sets

        @param  fds The dictionary of lhs's : rhs's
        @retval A list of pairs of sets, representing the fds
    """

    # Parse the dictionary of user inputs into a list of pairs of sets
    old_fds = fds
    fds = []
    for old_fd in old_fds.keys():
        if "," in old_fd:
            left_side = split(old_fd, ",")
        else:
            left_side = [old_fd]
            left_side = left_side
        if "," in old_fds[old_fd]:
            right_side = split(old_fds[old_fd],",")
        else:
            right_side = [old_fds[old_fd]]

        # Create sets from the right and left side, and add this pair of sides to the list of fds
        if left_side != [''] and right_side != [''] and left_side != [] and right_side != [] and left_side is not None and right_side is not None:
            left_side_set = set(left_side)
            right_side_set = set(right_side)
            self_fd = (left_side_set, right_side_set)
            if self_fd not in fds:
                fds.append(self_fd)

    # Build a set containing all elements
    elements = set()
    for fd in fds:
        for element in fd[1]:
            element = element.strip()
            if element not in elements:
                elements.add(element)
        for element in fd[0]:
            element = element.strip()
            if element not in elements:
                elements.add(element)

    # Remove duplicates
    elements = set(list(elements))

    return fds, elements


def find_closure(fds, elements):
    """
      Finds the closure of a set of elements given a set of FDs

        @param  fds  The set of functional dependencies
        @param  elements    The set of elements to determine the closure of
        @retval The closure of these elements with these FDs, as a set
    """

    # Create a flag for whether or not we've modified the closure in this iteration
    modified = True

    # Loop over the closure until we haven't modified it in the last iteration
    while modified:

        # Assume we're not going to modify anything...
        modified = False

        # For each fd in our list of fds, apply reflexivity
        new_fds = deepcopy(fds)
        for fd in fds:

            # If the left side isn't already a subset of the right side
            if not fd[0].issubset(fd[1]):
                modified = True
                new_fds.remove(fd)
                fd_right = fd[1].union(fd[0])
                new_fds.append((fd[0], fd_right))
        fds = new_fds

        # Apply augmentation rules to each pair of FDs
        new_fds = deepcopy(fds)
        added_fds = []
        for first_fd in fds:
            for second_fd in fds:

                # Build a new fd combining these two
                new_fd_left = first_fd[0].union(second_fd[0])
                new_fd_right = first_fd[1].union(second_fd[1])
                new_proposed_fd = (new_fd_left, new_fd_right)

                # Check to see if this one exists, if it doesn't, add it
                if new_proposed_fd not in fds and new_proposed_fd not in added_fds:
                    modified = True
                    new_fds.append(new_proposed_fd)
                    added_fds.append(new_proposed_fd)
        fds = new_fds

        # Apply transitive rule to each pair of FDs
        new_fds = deepcopy(fds)
        added_fds = []
        for first_fd in fds:
            for second_fd in fds:

                # If the right side of the first is a subset of the left side of the second, combine them..
                if second_fd[0].issubset(first_fd[1]) and first_fd != second_fd:

                    # Build a new fd combining these two
                    new_fd_left = first_fd[0]
                    new_fd_right = first_fd[1].union(second_fd[1])
                    new_proposed_fd = (new_fd_left, new_fd_right)

                    # Add it if its not already in our list
                    if new_proposed_fd not in fds and new_proposed_fd not in added_fds:

                        # Flag this list as modified
                        modified = True
                        added_fds.append(new_proposed_fd)
                        new_fds.append(new_proposed_fd)

        fds = new_fds

    # Compute the closure of all these FDs
    closure = set()
    for fd in fds:
        if fd[0].issubset(elements):
            closure = closure.union(fd[1])

    # Return the closure of these elements
    return closure



def fd_closure_demo():
    """
      Compute the closure of an input set of FDs
    """

    # Build the dictionary of FDs
    print "FD Closure Demo:"
    fds = build_fds()

    # Convert the FDs into a list of pairs of sets
    fds, elements = convert_fds(fds)

    # For each variable 'A', add an FD that's A->A
    for element in elements:
        simple_fd = (set([element]), set([element]))
        if simple_fd not in fds:
            fds.append(simple_fd)

    done = False
    while not done:

        # Ask the user to select which subset of the given elements we'd like to investigate
        valid = False
        while not valid:

            # Prompt for user input
            input = str(raw_input("Enter the set of elements that you would like to compute the closure of as X,X,X..., or type 'done' to return to the previous menu\n"))
            input = input.rstrip().lstrip()


            # If we asked to return
            if input != "done":

                # Parse the input into a set
                element_strings = split(input, ",")
                search_elements = set()
                if type(element_strings) == type(list()):
                    for element_string in element_strings:
                        element_string = element_string.rstrip().lstrip()
                        search_elements.add(element_string)
                else:
                    element_strings = element_strings.rstrip().lstrip()
                    search_elements.add(element_strings)

                # Check that the input set is a subset of the elements we have
                if not search_elements.issubset(elements):
                    print "Sorry, it looks like not all those elements were input..."
                    valid = False
                else:
                    valid = True

            else:
                valid = True
                done = True

        if not done:

            # Find the closure of this set of elements given these FDs
            closure = find_closure(fds, search_elements)

            # Output the closure
            closure_string = ""
            for element in closure:
                closure_string += element + ", "
            closure_string = closure_string.rstrip(", ")
            print "The closure of that set is: %s" % closure_string


def build_powerset(s):
    """
      Generates the powerset of a set
    """
    d = dict(
            zip((1<<i for i in range(len(s))),
            (set([e]) for e in s)
            ))
    subset = set()
    yield subset
    for i in range(1, 1<<len(s)):
        subset = subset ^ d[i & -i]
        yield subset


choice = -1

while choice != 2:

    # Prompt for user input
    print "What would you like to do?"
    print "     1)  FD Closure Demo"
    print "     2)  Quit"

    # Get user input and call the right routine
    try:
        choice = int(raw_input("Please enter a number below: \n"))

    except:
        print "Please enter a number!"
        choice = -1

    # Perform the corresponding action
    if choice is 1:
        fd_closure_demo()

print "Bye!"
