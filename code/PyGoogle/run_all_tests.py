"""
  Runs all tests in the 'Test' module and its subdirectories
"""
__author__ = 'Jon Tedesco'

from nose.plugins.plugintest import run_buffered as run

run(argv=['nosetests', '-v', 'Test'])