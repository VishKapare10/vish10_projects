import logging

logging.basicConfig(filename='./app.log', filemode='w', format='%(name)s - %(levelname)s - %(message)s')
logging.warning('Inside log file....!!!')
