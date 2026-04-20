#
#  FreeSynd - a remake of the classic Bullfrog game "Syndicate".
#
#   Copyright (C) 2023-2025  Benoit Blancard <benblan@users.sourceforge.net>
#
#   This program is free software: you can redistribute it and/or
#  modify it under the terms of the GNU General Public License as 
#  published by the Free Software Foundation, either version 3 of the
#  License, or (at your option) any later version.
#
#   This program is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of 
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
#  See the GNU General Public License for more details.
#
#   You should have received a copy of the GNU General Public License
#  along with this program. If not, see <https://www.gnu.org/licenses/>. 
# 

cmake_minimum_required (VERSION 3.22.1)

project(FreeSynd VERSION 0.958 LANGUAGES C CXX)

set(CMAKE_CXX_STANDARD 20 CACHE STRING "The C++ standard to use")
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

if (NOT CMAKE_BUILD_TYPE AND NOT CMAKE_CONFIGURATION_TYPES)
  message(STATUS "Setting build type to 'Debug' as none was specified.")
  set(CMAKE_BUILD_TYPE Debug CACHE STRING "Choose the type of build." FORCE)

  # Set the possible values of build type for cmake-gui, ccmake
  set_property(CACHE CMAKE_BUILD_TYPE PROPERTY STRINGS "Debug" "Release" "RelWithDebInfo")
endif ()

set(CMAKE_MODULE_PATH "${CMAKE_CURRENT_SOURCE_DIR}/cmake")

configure_file(${CMAKE_CURRENT_SOURCE_DIR}/version.h.in ${CMAKE_CURRENT_SOURCE_DIR}/game/version.h)

if (ANDROID)
    add_definitions(-DANDROID)
    find_library(log-lib log)

    set(lib_path "${CMAKE_SOURCE_DIR}/../platform/android/app/src/main/jniLibs/${ANDROID_ABI}")
    set(inc_path "${CMAKE_SOURCE_DIR}/includes")

    # 1. FORCE variables into the Cache so subdirectories (engine/) see them
    # These names must match what FindSDL2_image.cmake expects
    set(SDL2_IMAGE_LIBRARY "${lib_path}/libSDL2_image.so" CACHE FILEPATH "" FORCE)
    set(SDL2_IMAGE_INCLUDE_DIR "${inc_path}/SDL2_image/include" CACHE PATH "" FORCE)
    set(SDL2_IMAGE_FOUND TRUE CACHE BOOL "" FORCE)

    # SDL2_image depends on SDL2, so we must force SDL2 as well
    set(SDL2_LIBRARY "${lib_path}/libSDL2.so" CACHE FILEPATH "" FORCE)
    set(SDL2_INCLUDE_DIR "${inc_path}/SDL2/include" CACHE PATH "" FORCE)
    set(SDL2_FOUND TRUE CACHE BOOL "" FORCE)

    # PNG is often a dependency of SDL2_image
    set(PNG_LIBRARY "${lib_path}/libpng.so" CACHE FILEPATH "" FORCE)
    set(PNG_PNG_INCLUDE_DIR "${inc_path}/libpng/include" CACHE PATH "" FORCE)
    set(PNG_FOUND TRUE CACHE BOOL "" FORCE)

    # --- SDL2_MIXER BLOCK ---
    set(SDL2_MIXER_LIBRARIES "${lib_path}/libSDL2_mixer.so" CACHE FILEPATH "" FORCE)
    set(SDL2_MIXER_LIBRARY "${lib_path}/libSDL2_mixer.so" CACHE FILEPATH "" FORCE) # Often search scripts look for singular too
    set(SDL2_MIXER_INCLUDE_DIRS "${inc_path}/SDL2_mixer/include" CACHE PATH "" FORCE)
    set(SDL2_MIXER_INCLUDE_DIR "${inc_path}/SDL2_mixer/include" CACHE PATH "" FORCE)
    set(SDL2_MIXER_FOUND TRUE CACHE BOOL "" FORCE)

    # 2. Provide the include directories for the current scope
    include_directories(${inc_path})
    include_directories(${inc_path}/SDL2/include)
    include_directories(${inc_path}/SDL2_image/include)
    include_directories(${inc_path}/SDL2_mixer/include)
    include_directories(${inc_path}/libpng/include)

    # 3. Define the Imported Targets for linking later
    add_library(SDL2 SHARED IMPORTED)
    set_target_properties(SDL2 PROPERTIES IMPORTED_LOCATION "${lib_path}/libSDL2.so")

    add_library(SDL2_image SHARED IMPORTED)
    set_target_properties(SDL2_image PROPERTIES IMPORTED_LOCATION "${lib_path}/libSDL2_image.so")

    add_library(libpng SHARED IMPORTED)
    set_target_properties(libpng PROPERTIES IMPORTED_LOCATION "${lib_path}/libpng.so")

    add_library(SDL2_mixer SHARED IMPORTED)
    set_target_properties(SDL2_mixer PROPERTIES IMPORTED_LOCATION "${lib_path}/libSDL2_mixer.so")

    # 7. Create your native test library
    #add_library(freesynd SHARED freesynd.cpp)

    # 8. Link ALL of them
    #target_link_libraries(freesynd SDL2 SDL2_image SDL2_mixer log android)
endif()

if (WIN32)
	# Search the bundled libs directory when using MSVC.
	set (CMAKE_LIBRARY_PATH ${CMAKE_LIBRARY_PATH}
		${CMAKE_SOURCE_DIR}/extern/sdl2/lib/${FREESYND_ARCH}
        ${CMAKE_SOURCE_DIR}/extern/SDL2_mixer/lib/${FREESYND_ARCH}
        ${CMAKE_SOURCE_DIR}/extern/SDL2_image/lib/${FREESYND_ARCH}
	)
	set (CMAKE_INCLUDE_PATH ${CMAKE_INCLUDE_PATH}
		${CMAKE_SOURCE_DIR}/extern/sdl2/include
        ${CMAKE_SOURCE_DIR}/extern/SDL2_mixer/include
        ${CMAKE_SOURCE_DIR}/extern/SDL2_image/include        
	)
    # Adds CLI,CRCpp,UTF8cpp and zlib  headers
    set(inc_extern_path "${CMAKE_SOURCE_DIR}")
    include_directories(${inc_extern_path}/includes)
endif ()
        
#################################
# Compilation flags management
#################################

if (MSVC)
	# Add extra definitions just for MSVC.
	add_definitions (-D_CONSOLE -D_CRT_SECURE_NO_WARNINGS)
	# Override default flags for release builds.
	set (CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE} /O2 /Oi /Ot /GL")
	set (CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} /O2 /Oi /Ot /GL")
	set (CMAKE_EXE_LINKER_FLAGS_RELEASE "${CMAKE_EXE_LINKER_FLAGS_RELEASE} /LTCG")
endif ()

include(cmake/CompilerWarnings.cmake)

add_library(freesynd_warnings INTERFACE)

set_project_warnings(freesynd_warnings)

#################################
# Installation management
#################################

# Using GNU Coding Standards
include(GNUInstallDirs)

# FS_ETC_DIR controls the location of the freesynd.ini directory
if (NOT FS_ETC_DIR)
    if (UNIX)
        SET(FS_ETC_DIR "${CMAKE_INSTALL_FULL_SYSCONFDIR}/freesynd")
    elseif (WIN32)
        SET(FS_ETC_DIR "${CMAKE_INSTALL_PREFIX}")
    endif()
endif (NOT FS_ETC_DIR)

# FS_DATA_DIR controls the location of the freesynd data
if (NOT FS_DATA_DIR)
    if (UNIX)
        SET(FS_DATA_DIR "${CMAKE_INSTALL_FULL_DATADIR}/freesynd")
    elseif (WIN32)
        SET(FS_DATA_DIR "${CMAKE_INSTALL_PREFIX}")
    endif()
endif (NOT FS_DATA_DIR)

#################################
# Specify the project's components
#################################

# Docs only available if this is the main app
find_package(Doxygen)
if(Doxygen_FOUND)
    add_subdirectory(docs)
else()
    message(STATUS "Doxygen not found, not building docs")
endif()

if(BUILD_TESTING)
    find_package(Catch2 3 REQUIRED)
    if(CMAKE_PROJECT_NAME STREQUAL PROJECT_NAME)
        include(CTest)
        include(Catch)
    endif()
endif()


# Build the sources in these subdirectory.
add_subdirectory (packaging)
add_subdirectory (data)
add_subdirectory (utils)
add_subdirectory (engine)
add_subdirectory (kernel)
add_subdirectory (game)

if(CMAKE_BUILD_TYPE STREQUAL "Debug")
    # The editor is build only in Development mode
    add_subdirectory (editor)
endif()

#################################
# For all packaging instructions
# Only in Release mode
#################################
if(CMAKE_BUILD_TYPE STREQUAL "Release")
    include(Packing)
endif()

