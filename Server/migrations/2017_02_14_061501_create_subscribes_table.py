from orator.migrations import Migration


class CreateSubscribesTable(Migration):

    def up(self):
        """
        Run the migrations.
        """
        with self.schema.create('subscribes') as table:
            table.increments('id')
            table.integer('subscriber_id').unsigned()
            table.integer('subscribed_id').unsigned()
            table.timestamps()

            table.foreign('subscriber_id').references('id').on('users').on_delete('cascade')
            table.foreign('subscribed_id').references('id').on('blogs').on_delete('cascade')


    def down(self):
        """
        Revert the migrations.
        """
        self.schema.drop('subscribes')
