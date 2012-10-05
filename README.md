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

Contributing
------------

If you have some time to spare on an useless project and would like to help take a look at the [list of issues](http://github.com/borisguery/PaginatedResource/issues).


Authors
-------

Boris Guéry - <guery.b@gmail.com> - <http://twitter.com/borisguery> - <http://borisguery.com>

License
-------

`Symfony2 Console Autolookup` is licensed under the WTFPL License - see the LICENSE file for details

