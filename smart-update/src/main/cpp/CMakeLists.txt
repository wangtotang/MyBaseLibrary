cmake_minimum_required(VERSION 3.4.1)



# 将当前 "./src/main/cpp" 目录下的所有源文件保存到 "NATIVE_SRC" 中，然后在 add_library 方法调用。
aux_source_directory( . native_src )
# 将 "./src/main/cpp/bzip2" 目录下的子目录bzip2保存到 "BZIP2_BASE" 中，然后在 add_library 方法调用。
aux_source_directory( ./bzip2 bzip2_src )
# 将 BZIP2_BASE 增加到 NATIVE_SRC 中，这样目录的源文件也加入了编译列表中，当然也可以不加到 NATIVE_SRC，直接调用add_library。
list(APPEND native_src ${bzip2_src})

add_library( # Sets the name of the library.
             smart-update
             # Sets the library as a shared library.
             SHARED
             # Provides a relative path to your source file(s).
             ${native_src} )

find_library( # Defines the name of the path variable that stores the
              # location of the NDK library.
              log-lib

              # Specifies the name of the NDK library that
              # CMake needs to locate.
              log )


target_link_libraries( # Specifies the target library.
                       smart-update

                       # Links the log library to the target library.
                       ${log-lib} )