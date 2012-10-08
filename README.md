Symfony2 Console Autolookup
=======================

Table of contents
-----------------

1. [Description](#description)
2. [Installation](#installation)
3. [Usage](#usage)
4. [Contributing](#contributing)
5. [Authors](#authors)
6. [License](#license)

Description
-----------

The idea of this little snippet is to be able to run the Symfony2 `console` from anywhere in the directory
tree, without the need to move on the right folder.

It is similar to `git` at use as it can be ran from any of the subfolder.

Installation
------------

Just clone the repository and move the file into `~/bin` (make sure it is in your `$PATH`)

```bash
$ git clone git://github.com/borisguery/sf2console-autolookup.git && cd sf2console-autolookup
$ mv sf2console ~/bin/
$ chmod +x ~/bin/sf2console
```

Usage
-----

Let's say you're currently in `sf2projectroot/app/cache/twig` for some debugging purpose
```bash
$ sf2console twig:lint somefile
```

It will run `sf2projectroot/app/console`

If you move into another project, you can re-run the same command and the according console
will be run.

Tips
----

This tip is not only applicable to this script but most of the little tools you may find.

Make a `bin/` directory into your home (may it be `/home/username` or `/Users/username`)

```bash
$ mkdir ~/bin
```

Then add it to your `$PATH` env variable, in your `.bash_profile` (or whatever it may be)

```
PATH=$PATH:~/bin
```

Autocomplete
------------

It is possible to autocomplete your command according to the path found by the script.
It means that the result of the completion will vary depending on your path.

To enable it, move the `sf2console.complete` into your completion directory. It should be either
`/etc/bash_completion.d` or `/opt/local/etc/bash_completion.d`.

Make sure to `source /etc/bash_completion.d/sf2console.complete` the moved file to load the autocompletion.

*Note that it is currently a bit slow, because the path needs to be computed on each call*

Contributing
------------

If you have some time to spare on an useless project and would like to help take a look at the [list of issues](http://github.com/borisguery/PaginatedResource/issues).


Authors
-------

Boris Guéry - <guery.b@gmail.com> - <http://twitter.com/borisguery> - <http://borisguery.com>

License
-------

`Symfony2 Console Autolookup` is licensed under the WTFPL License - see the LICENSE file for details

