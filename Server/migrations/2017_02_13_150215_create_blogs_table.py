from orator.migrations import Migration


class CreateBlogsTable(Migration):

    def up(self):
        """
        Run the migrations.
        """
        with self.schema.create('blogs') as table:
            table.increments('id')
            table.string('url').unique()
            table.timestamps()

    def down(self):
        """
        Revert the migrations.
        """
        self.schema.drop('blogs')
