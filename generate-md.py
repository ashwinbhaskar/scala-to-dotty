import pathlib
from functools import reduce


def get_all_files(root):
    all_files = []
    for path in root.iterdir():
        if path.is_file():
            all_files.append(path)
        elif path.is_dir():
            all_files += get_all_files(path)
    return all_files

def match_files(scala_files, dotty_files):
    matched_files = []
    for scala_file in scala_files:
        pair = ()
        for dotty_file in dotty_files:
            if scala_file.name == dotty_file.name:
                pair = (scala_file, dotty_file)
                dotty_files.remove(dotty_file)
                break
        if len(pair) == 2:
            matched_files.append(pair)
    return matched_files


dotty_core = pathlib.Path("./src/main/scala/dotty/core")
scala_core = pathlib.Path("./src/main/scala/scala2/core")

all_dotty_core_files = get_all_files(dotty_core)
all_scala_core_files = get_all_files(scala_core)
matched_files = match_files(all_scala_core_files, all_dotty_core_files)

with open("README.md", "w") as f:
    with open("top-of-readme.md", "r") as top_of_readme:
        f.write(top_of_readme.read() + "\n")
    for (scala_file, dotty_file) in matched_files:
        f.write("###" + scala_file.name + "\n")
        with open(scala_file, "r") as open_scala_file:
            f.write("**Scala2 version**\n")
            f.write("```scala\n")
            f.write(open_scala_file.read() + "\n")
            f.write("```\n")
        with open(dotty_file, "r") as open_dotty_file:
            f.write("**Dotty version**\n")
            f.write("```scala\n")
            f.write(open_dotty_file.read() + "\n")
            f.write("```\n")
