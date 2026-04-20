# Distributed under the OSI-approved BSD 3-Clause License.  See accompanying
# file LICENSE.rst or https://cmake.org/licensing for details.

cmake_minimum_required(VERSION ${CMAKE_VERSION}) # this file comes with cmake

# If CMAKE_DISABLE_SOURCE_CHANGES is set to true and the source directory is an
# existing directory in our source tree, calling file(MAKE_DIRECTORY) on it
# would cause a fatal error, even though it would be a no-op.
if(NOT EXISTS "K:/Coding/FreeSynd/platform/windows/_deps/libadl-src")
  file(MAKE_DIRECTORY "K:/Coding/FreeSynd/platform/windows/_deps/libadl-src")
endif()
file(MAKE_DIRECTORY
  "K:/Coding/FreeSynd/platform/windows/_deps/libadl-build"
  "K:/Coding/FreeSynd/platform/windows/_deps/libadl-subbuild/libadl-populate-prefix"
  "K:/Coding/FreeSynd/platform/windows/_deps/libadl-subbuild/libadl-populate-prefix/tmp"
  "K:/Coding/FreeSynd/platform/windows/_deps/libadl-subbuild/libadl-populate-prefix/src/libadl-populate-stamp"
  "K:/Coding/FreeSynd/platform/windows/_deps/libadl-subbuild/libadl-populate-prefix/src"
  "K:/Coding/FreeSynd/platform/windows/_deps/libadl-subbuild/libadl-populate-prefix/src/libadl-populate-stamp"
)

set(configSubDirs Debug)
foreach(subDir IN LISTS configSubDirs)
    file(MAKE_DIRECTORY "K:/Coding/FreeSynd/platform/windows/_deps/libadl-subbuild/libadl-populate-prefix/src/libadl-populate-stamp/${subDir}")
endforeach()
if(cfgdir)
  file(MAKE_DIRECTORY "K:/Coding/FreeSynd/platform/windows/_deps/libadl-subbuild/libadl-populate-prefix/src/libadl-populate-stamp${cfgdir}") # cfgdir has leading slash
endif()
