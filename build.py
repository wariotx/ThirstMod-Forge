import os, os.path, sys
import urllib, zipfile
import shutil, glob, fnmatch
import subprocess, logging, re
import csv, shutil
import pprint
import distutils

mcp_dir = os.path.abspath('.')

def main():
    print '-> Thirst Mod Build Starting...'
    
    src_dir = os.path.join(mcp_dir, 'src')
    common_dir = os.path.join(src_dir, 'common')
    client_dir = os.path.join(src_dir, 'minecraft')
    
    tm_dir = os.path.join(mcp_dir, 'thirstmod')
    tm_common = os.path.join(common_dir, 'tarun1998')
    tm_client= os.path.join(client_dir, 'tarun1998')
    
    build_dir = os.path.join(tm_dir, 'build')
    build_common_dir = os.path.join(build_dir, 'common')
    build_client_dir = os.path.join(build_dir, 'client')
    
    # Cleans the /mcp/thirstmod/ folder
    if os.path.isdir(build_dir):
        shutil.rmtree(build_dir)
    
    # Remakes all the directories
    os.mkdir(build_dir)
    os.mkdir(build_common_dir)
    os.mkdir(build_client_dir)
    
    tm_common_dir = os.path.join(build_common_dir, 'tarun1998')
    tm_client_dir = os.path.join(build_client_dir, 'tarun1998')

    # Copies the mod source from /mcp/src/ to my own custom folder.
    shutil.copytree(tm_common, tm_common_dir)
    shutil.copytree(tm_client, tm_client_dir)
    
    # Copies the resources to the build folder.
    resource_dir = os.path.join(tm_dir, 'resources')
    other_dir = os.path.join(tm_dir, 'other')
    new_resource_dir = os.path.join(build_dir, 'resources')
    shutil.copytree(resource_dir, new_resource_dir)
    
    # Copies the other stuff I have uploaded to Github to the build folder.
    eclipse_formatter_dir = os.path.join(other_dir, 'eclipse_formatter.xml')
    readme_dir = os.path.join(other_dir, 'README.md')
    buildscript_sh_dir = mcp_dir + '/build.sh'
    buildscript_py_dir = mcp_dir + '/build.py'
    new_formatter_dir = os.path.join(build_dir, 'eclipse_formatter.xml')
    new_readme_dir = os.path.join(build_dir, 'README.md')
    new_buildscript_sh_dir = build_dir + '/build.sh'
    new_buildscript_py_dir = build_dir + '/build.py'
    shutil.copyfile(eclipse_formatter_dir, new_formatter_dir)
    shutil.copyfile(readme_dir, new_readme_dir)
    shutil.copyfile(buildscript_sh_dir, new_buildscript_sh_dir)
    shutil.copyfile(buildscript_py_dir, new_buildscript_py_dir)
    
    print '-> Preparing latest release zip'
    
    # Begins producing the 'latest build.zip' file.
    reobf_mc_dir = mcp_dir + '/reobf/minecraft/tarun1998'
    cache_dir = tm_dir + "/cache"
    cache_tarun1998 = cache_dir + '/ThirstMod/Put contents in mods folder/ThirstMod/tarun1998'
    os.mkdir(cache_dir)
    os.mkdir(cache_dir + '/ThirstMod/')
    os.mkdir(cache_dir + '/ThirstMod/Put contents in mods folder/')
    os.mkdir(cache_dir + '/ThirstMod/Put contents in mods folder/ThirstMod/')
    shutil.copytree(reobf_mc_dir, cache_tarun1998)
    
    textures_dir = resource_dir + "/mods/ThirstMod/tarun1998/thirstmod/textures"
    shutil.copytree(textures_dir, cache_tarun1998 + '/thirstmod/textures')
    
    base_dir = cache_dir + '/ThirstMod/Put contents in mods folder/ThirstMod/'
    content_dir = resource_dir + '/mods/ThirstMod/Content'
    shutil.copytree(content_dir, base_dir + 'Content')
    
    config_dir = resource_dir + '/mods/ThirstMod/' + "Config.txt"
    mcmod_dir =  resource_dir + '/mods/ThirstMod/' + "mcmod.info"
    shutil.copy(config_dir, base_dir + 'Config.txt')
    shutil.copy(mcmod_dir, base_dir + 'mcmod.info')
    
    zip_dir(cache_dir + '/ThirstMod', build_dir + '/latest build.zip')
    # Ends producing 'latest build.zip' file.
    
    print '-> Retrieved zip file!'
    print '-> Thirst Mod build ended'
    
    # Deletes the cache folder as we no longer need it.
    shutil.rmtree(cache_dir)

# Taken from http://stackoverflow.com/questions/458436/adding-folders-to-a-zip-file-using-python
def zip_dir(dirpath, zippath):
    fzip = zipfile.ZipFile(zippath, 'w', zipfile.ZIP_DEFLATED)
    basedir = os.path.dirname(dirpath) + '/' 
    for root, dirs, files in os.walk(dirpath):
        if os.path.basename(root)[0] == '.':
            continue #skip hidden directories        
        dirname = root.replace(basedir, '')
        for f in files:
            if f[-1] == '~' or (f[0] == '.' and f != '.htaccess'):
                #skip backup files and all hidden files except .htaccess
                continue
            fzip.write(root + '/' + f, dirname + '/' + f)
    fzip.close()
    
if __name__ == '__main__':
    main()